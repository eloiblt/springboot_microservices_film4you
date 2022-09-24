package fr.cpe.filmforyou.filmcore.service.impl;

import com.mongodb.client.MongoCursor;
import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.filmcore.document.*;
import fr.cpe.filmforyou.filmcore.mapper.FilmMapper;
import fr.cpe.filmforyou.filmcore.repository.FilmRepository;
import fr.cpe.filmforyou.filmcore.service.FilmService;
import fr.cpe.filmforyou.filmlib.dto.*;
import fr.cpe.filmforyou.preferencelib.dto.PreferenceDTO;
import fr.cpe.filmforyou.preferencelib.webservice.PreferenceWebService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class FilmServiceImpl implements FilmService {

    private final Logger logger = LoggerFactory.getLogger(FilmServiceImpl.class);

    private static final String COUNT_FIELD_NAME = "count";
    private static final String GENRE_FIELD_NAME = "genre";
    private static final String FILMS_COLLECTION_NAME = "films";
    private static final String COLLECT_SUMMARY_EXCEPTION = "Erreur lors de la récupération des valeurs";

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final PreferenceWebService preferenceWebService;
    private final MongoTemplate mongoTemplate;

    public FilmServiceImpl(FilmRepository filmRepository, FilmMapper filmMapper, PreferenceWebService preferenceWebService, MongoTemplate mongoTemplate) {
        this.filmRepository = filmRepository;
        this.filmMapper = filmMapper;
        this.preferenceWebService = preferenceWebService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Cacheable("findAllInList")
    public List<FilmLightDTO> findAllInList(List<String> ids) {
        Query query = new Query().addCriteria(Criteria.where("id").in(ids));
        List<FilmLight> filmLightList = this.mongoTemplate.find(query, FilmLight.class);
        return this.filmMapper.toLightFromLightList(filmLightList);
    }

    @Override
    @Cacheable("findById")
    public FilmDTO findById(String filmId) {
        return this.filmMapper.toDTO(this.filmRepository.findById(filmId).orElseThrow(() -> new FilmForYouException(String.format("Le film d'id %s n'existe pas en base de données", filmId), HttpStatus.NOT_FOUND)));
    }

    @Override
    public List<FilmLightDTO> getReleased(Long userId) {
        int releaseSize = 20;
        Pageable pageable = getOrderForRelease(releaseSize);
        List<FilmLightDTO> filmUserDTOS = this.filmMapper.toLightDTOList(this.filmRepository.findAllWithNoDefaultUrl(pageable).getContent());
        return this.populateNote(filmUserDTOS, userId);
    }

    @Override
    public List<FilmLightDTO> getMostLoved(Long userId) {
        int mostLovedSize = 20;
        Pageable pageable = getOrderForMostLoved(mostLovedSize);
        List<FilmLightDTO> filmUserDTOS = this.filmMapper.toLightDTOList(this.filmRepository.findAllWithNoDefaultUrl(pageable).getContent());
        return this.populateNote(filmUserDTOS, userId);
    }

    @Override
    public List<FilmLightDTO> getPopular(Long userId) {
        int popularSize = 20;
        Pageable pageable = getOrderForPopular(popularSize);
        List<FilmLightDTO> filmUserDTOS = this.filmMapper.toLightDTOList(this.filmRepository.findAllWithNoDefaultUrl(pageable).getContent());
        return this.populateNote(filmUserDTOS, userId);
    }

    private List<FilmLightDTO> populateNote(List<FilmLightDTO> filmIds, Long userId) {
        List<PreferenceDTO> preferenceDTOS = this.getPreferenceForIDs(filmIds.stream().map(FilmLightDTO::getId).collect(Collectors.toList()), userId);
        preferenceDTOS.forEach(preferenceDTO -> filmIds.stream().filter(filmUserDTO -> filmUserDTO.getId().equals(preferenceDTO.getFilmId())).findFirst().ifPresent(filmUserDTO -> filmUserDTO.setUserNote(preferenceDTO.getMark())));
        return filmIds;
    }

    public List<PreferenceDTO> getPreferenceForIDs(List<String> filmIds, Long userId) {
        return this.preferenceWebService.getPreferencesByFilmIdsAndUser(userId, filmIds);
    }

    @Override
    public List<FilmDTO> getAll() {
        logger.info("Start collect all films");
        List<Film> films = this.filmRepository.findAll();
        logger.info("All films collected");
        return this.filmMapper.toDTOList(films);
    }

    private Pageable getOrderForRelease(int size) {
        return PageRequest.of(0, size, Sort.by("datePublished").descending());
    }

    private Pageable getOrderForMostLoved(int size) {
        return PageRequest.of(0, size, Sort.by("avgVote").descending());
    }

    private Pageable getOrderForPopular(int size) {
        return PageRequest.of(0, size, Sort.by("votes").descending());
    }

    public List<FilmSearchResponseDTO> search(String searchQuery, FilmSearchRequestDTO filmSearchRequestDTO) {

        Query query = TextQuery.queryText(TextCriteria.forDefaultLanguage()
                        .matching(searchQuery))
                .sortByScore();

        if (filmSearchRequestDTO != null && filmSearchRequestDTO.getGenre() != null) {
            query.addCriteria(Criteria.where(GENRE_FIELD_NAME).is(filmSearchRequestDTO.getGenre()));
        }

        if (filmSearchRequestDTO != null && filmSearchRequestDTO.getYear() != null) {
            query.addCriteria(Criteria.where("year").is(String.valueOf(filmSearchRequestDTO.getYear())));
        }

         query = query.with(PageRequest.of(0, 5));

        List<FilmSearch> films = this.mongoTemplate.find(query, FilmSearch.class);

        return this.filmMapper.toFilmSearchResponseList(films);
    }

    @Override
    @Cacheable("filmExists")
    public Boolean exists(String filmId) {
        return this.filmRepository.existsById(filmId);
    }

    @Override
    public FilmSummaryDTO getSummary(FilmSummaryRequestDTO filmSummaryRequestDTO) {

        FilmSummaryDTO filmSummaryDTO = new FilmSummaryDTO();

        if (filmSummaryRequestDTO.getFilmsViewed().isEmpty()) {
            filmSummaryDTO.setTotalDuration(0L);
        } else {
            Future<Long> totalDuration = this.getTotalDuration(filmSummaryRequestDTO);

            try {
                filmSummaryDTO.setTotalDuration(totalDuration.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new FilmForYouException(COLLECT_SUMMARY_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (ExecutionException e) {
                throw new FilmForYouException(COLLECT_SUMMARY_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (filmSummaryRequestDTO.getFilmsLiked().isEmpty()) {
            String unknown = "unknown";
            filmSummaryDTO.setGenre(unknown);
            filmSummaryDTO.setActor(unknown);
            filmSummaryDTO.setDirector(unknown);
            filmSummaryDTO.setProduction(unknown);

            return filmSummaryDTO;
        }

        Future<String> genre = this.getFavoriteGenre(filmSummaryRequestDTO);
        Future<String> actor = this.getFavoriteActor(filmSummaryRequestDTO);
        Future<String> direction = this.getFavoriteDirector(filmSummaryRequestDTO);
        Future<String> production = this.getFavoriteProduction(filmSummaryRequestDTO);

        try {
            filmSummaryDTO.setGenre(genre.get());
            filmSummaryDTO.setActor(actor.get());
            filmSummaryDTO.setDirector(direction.get());
            filmSummaryDTO.setProduction(production.get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FilmForYouException(COLLECT_SUMMARY_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ExecutionException e) {
            throw new FilmForYouException(COLLECT_SUMMARY_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return filmSummaryDTO;
    }

    private Aggregation getAggregationUnwindByName(FilmSummaryRequestDTO filmSummaryRequestDTO, String name) {
        MatchOperation matchOperation = this.filterOnFilms(filmSummaryRequestDTO.getFilmsLiked());
        UnwindOperation unwindOperation = unwind(name);
        GroupOperation groupByActor = group(name).count().as(COUNT_FIELD_NAME);
        SortOperation sortByCount = sort(Sort.Direction.DESC, COUNT_FIELD_NAME).and(Sort.Direction.ASC, "_id");
        GroupOperation getFirstElement = group().first("_id").as("favoriteValue");

        return newAggregation(matchOperation, unwindOperation, groupByActor, sortByCount, getFirstElement);
    }

    private String collectValueFromAggragation(Aggregation aggregation, String name) throws FilmForYouException {
        AggregationResults<FilmFavorite> result = mongoTemplate.aggregate(aggregation, FILMS_COLLECTION_NAME, FilmFavorite.class);

        FilmFavorite filmFavorite = result.getUniqueMappedResult();

        if (filmFavorite == null) {
            throw new FilmForYouException(String.format("Erreur lors de la récupération de : %s", name), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return filmFavorite.getFavoriteValue();
    }

    @Async
    CompletableFuture<Long> getTotalDuration(FilmSummaryRequestDTO filmSummaryRequestDTO) throws FilmForYouException {

        MatchOperation matchOperation = this.filterOnFilms(filmSummaryRequestDTO.getFilmsViewed());
        GroupOperation groupFilmById = group("null").sum("duration").as("durationSum");

        Aggregation aggregation = newAggregation(matchOperation, groupFilmById);
        AggregationResults<FilmDuration> result = mongoTemplate.aggregate(aggregation, FILMS_COLLECTION_NAME, FilmDuration.class);

        FilmDuration filmDuration = result.getUniqueMappedResult();

        if (filmDuration == null) {
            throw new FilmForYouException("Erreur lors de la récupération de la durée totale de lecture", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return CompletableFuture.completedFuture(filmDuration.getDurationSum());
    }

    @Async
    CompletableFuture<String> getFavoriteGenre(FilmSummaryRequestDTO filmSummaryRequestDTO) throws FilmForYouException {

        MatchOperation matchOperation = this.filterOnFilms(filmSummaryRequestDTO.getFilmsLiked());
        GroupOperation groupByGenreOperation = group(GENRE_FIELD_NAME).count().as(COUNT_FIELD_NAME);
        SortOperation sortByCount = sort(Sort.Direction.DESC, COUNT_FIELD_NAME).and(Sort.Direction.ASC, "_id");
        GroupOperation getFirstElement = group().first("_id").as("favoriteValue");

        Aggregation aggregation = newAggregation(matchOperation, groupByGenreOperation, sortByCount, getFirstElement);
        AggregationResults<FilmFavorite> result = mongoTemplate.aggregate(aggregation, FILMS_COLLECTION_NAME, FilmFavorite.class);

        FilmFavorite filmFavorite = result.getUniqueMappedResult();

        if (filmFavorite == null) {
            throw new FilmForYouException("Erreur lors de la récupération du genre préféré", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return CompletableFuture.completedFuture(filmFavorite.getFavoriteValue());
    }

    @Async
    CompletableFuture<String> getFavoriteActor(FilmSummaryRequestDTO filmSummaryRequestDTO) throws FilmForYouException {
        Aggregation aggregation = getAggregationUnwindByName(filmSummaryRequestDTO, "actors");
        return CompletableFuture.completedFuture(this.collectValueFromAggragation(aggregation, "actors"));
    }

    @Async
    CompletableFuture<String> getFavoriteProduction(FilmSummaryRequestDTO filmSummaryRequestDTO) throws FilmForYouException {
        Aggregation aggregation = getAggregationUnwindByName(filmSummaryRequestDTO, "production_company");
        return CompletableFuture.completedFuture(this.collectValueFromAggragation(aggregation, "production_company"));
    }

    @Async
    CompletableFuture<String> getFavoriteDirector(FilmSummaryRequestDTO filmSummaryRequestDTO) throws FilmForYouException {
        Aggregation aggregation = getAggregationUnwindByName(filmSummaryRequestDTO, "writer");
        return CompletableFuture.completedFuture(this.collectValueFromAggragation(aggregation, "writer"));
    }

    MatchOperation filterOnFilms(List<String> filmIds) {
        return match(Criteria.where("_id").in(filmIds.stream().map(ObjectId::new).collect(Collectors.toList())));
    }

    @Cacheable("genres")
    public List<String> getGenres() {

        MongoCursor<String> cursor = this.mongoTemplate.getCollection(FILMS_COLLECTION_NAME).distinct(GENRE_FIELD_NAME, String.class).iterator();

        List<String> genreList = new LinkedList<>();

        while (cursor.hasNext()) {
            genreList.add(cursor.next());
        }

        return genreList;
    }
}
