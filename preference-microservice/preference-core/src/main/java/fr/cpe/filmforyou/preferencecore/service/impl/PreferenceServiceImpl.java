package fr.cpe.filmforyou.preferencecore.service.impl;

import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.filmlib.dto.FilmDTO;
import fr.cpe.filmforyou.filmlib.dto.FilmSummaryDTO;
import fr.cpe.filmforyou.filmlib.dto.FilmSummaryRequestDTO;
import fr.cpe.filmforyou.filmlib.webservice.FilmWebService;
import fr.cpe.filmforyou.preferencecore.bo.Preference;
import fr.cpe.filmforyou.preferencecore.bo.Watch;
import fr.cpe.filmforyou.preferencecore.mapper.PreferenceMapper;
import fr.cpe.filmforyou.preferencecore.repository.PreferenceRepository;
import fr.cpe.filmforyou.preferencecore.service.PreferenceService;
import fr.cpe.filmforyou.preferencecore.service.WatchService;
import fr.cpe.filmforyou.preferencelib.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    private final Logger logger = LoggerFactory.getLogger(PreferenceServiceImpl.class);

    private static final String NO_MARK_FOR_THIS_MOVIE = "Il n'existe pas encore de note pour ce film et cet utilisateur.";

    private final PreferenceRepository preferenceRepository;
    private final PreferenceMapper preferenceMapper;
    private final FilmWebService filmWebService;
    private final WatchService watchService;

    public PreferenceServiceImpl(PreferenceRepository preferenceRepository, PreferenceMapper preferenceMapper, FilmWebService filmWebService, WatchService watchService) {
        this.preferenceRepository = preferenceRepository;
        this.preferenceMapper = preferenceMapper;
        this.filmWebService = filmWebService;
        this.watchService = watchService;
    }

    @Override
    public List<PreferenceDTO> findAllByUserId(Long userId) {
        return this.preferenceMapper.toDTOList(this.preferenceRepository.findAllByUserId(userId));
    }

    @Cacheable("getFilmsDTO")
    public List<FilmDTO> getFilmsDTO(List<String> filmIds) {
        return this.filmWebService.findAllWithIds(filmIds);
    }

    @Override
    public List<PreferenceFullDTO> findAllByUserIdFull(Long userId) {

        List<Preference> preferenceList = this.preferenceRepository.findAllByUserId(userId);

        if (!preferenceList.isEmpty()) {
            List<FilmDTO> filmList = this.getFilmsDTO(preferenceList.stream().map(Preference::getFilmId).sorted().collect(Collectors.toList()));

            List<PreferenceFullDTO> preferenceFullDTOList = new ArrayList<>(preferenceList.size());

            preferenceList.forEach(preference -> {

                PreferenceFullDTO preferenceFullDTO = this.preferenceMapper.toFullDTO(preference);
                filmList.stream().filter(filmDTO -> filmDTO.getId().equals(preference.getFilmId())).findFirst().ifPresent(preferenceFullDTO::setFilm);
                preferenceFullDTOList.add(preferenceFullDTO);
            });

            return preferenceFullDTOList;
        }

        return new ArrayList<>(0);
    }

    @Override
    @Transactional
    public PreferenceDTO createPreference(CreatePreferenceDTO createPreferenceDTO, Long userId) throws FilmForYouException {
        Optional<Preference> optionalPreference = this.preferenceRepository.findOneByUserIdAndFilmId(userId, createPreferenceDTO.getFilmId());

        optionalPreference.ifPresent(preference -> {
            throw new FilmForYouException("Une note existe déjà pour ce film.", HttpStatus.BAD_REQUEST);
        });

        if (Boolean.FALSE.equals(this.filmWebService.exist(createPreferenceDTO.getFilmId()))) {
            throw new FilmForYouException("Ce film n'existe pas en base de données", HttpStatus.BAD_REQUEST);
        }

        this.watchService.removeIfExist(createPreferenceDTO.getFilmId(), userId);

        Preference preference = this.preferenceMapper.toBo(createPreferenceDTO);
        preference.setUserId(userId);

        logger.info("Create new preference : {}", preference);

        return this.preferenceMapper.toDTO(this.preferenceRepository.save(preference));
    }

    @Override
    public PreferenceDTO updatePreference(UpdatePreferenceDTO updatePreferenceDTO, Long userId) throws FilmForYouException {
        Preference preference = this.preferenceRepository.findOneByUserIdAndFilmId(userId, updatePreferenceDTO.getFilmId()).orElseThrow(() -> new FilmForYouException(NO_MARK_FOR_THIS_MOVIE, HttpStatus.BAD_REQUEST));
        preference.setMark(updatePreferenceDTO.getMark());

        logger.info("Update preference : {}", preference);

        return this.preferenceMapper.toDTO(this.preferenceRepository.save(preference));
    }

    @Override
    public PreferenceDTO findByUserIdAndFilmId(Long userId, String filmId) throws FilmForYouException {
        Preference preference = this.preferenceRepository.findOneByUserIdAndFilmId(userId, filmId).orElseThrow(() -> new FilmForYouException(NO_MARK_FOR_THIS_MOVIE, HttpStatus.NOT_FOUND));

        logger.info("Get user preference : userId : {}, filmId : {}", userId, filmId);

        return this.preferenceMapper.toDTO(preference);
    }

    @Override
    public PreferenceDTO deletePreference(Long userId, String filmId) throws FilmForYouException {
        Preference preference = this.preferenceRepository.findOneByUserIdAndFilmId(userId, filmId).orElseThrow(() -> new FilmForYouException(NO_MARK_FOR_THIS_MOVIE, HttpStatus.NOT_FOUND));

        logger.info("Delete preference : {}", preference);

        this.preferenceRepository.delete(preference);
        return this.preferenceMapper.toDTO(preference);
    }

    @Override
    public List<String> findFilmMostLoved() {
        return this.preferenceRepository.findFilmMostLoved(PageRequest.of(0, 20)).getContent();
    }

    @Override
    public PreferenceSummaryDTO getSummary(Long userId) {

        List<String> filmIds = this.preferenceRepository.getFilmLiked(userId);
        List<String> filmsViewed = this.preferenceRepository.getFilmViewed(userId);

        if (filmIds.isEmpty() && filmsViewed.isEmpty()) {
            String unknown = "Unknown";
            return PreferenceSummaryDTO.builder()
                    .actor(unknown)
                    .director(unknown)
                    .filmViewed((long) filmsViewed.size())
                    .genre(unknown)
                    .production(unknown)
                    .totalDuration(0L)
                    .build();
        }

        FilmSummaryRequestDTO filmSummaryRequestDTO = new FilmSummaryRequestDTO(filmIds, filmsViewed);
        FilmSummaryDTO filmSummary = this.filmWebService.getSummary(filmSummaryRequestDTO);

        return PreferenceSummaryDTO.builder()
                .actor(filmSummary.getActor())
                .genre(filmSummary.getGenre())
                .totalDuration(filmSummary.getTotalDuration())
                .director(filmSummary.getDirector())
                .production(filmSummary.getProduction())
                .filmViewed((long) filmsViewed.size())
                .build();
    }

    @Override
    @Transactional
    public void deleteAllFromUserId(Long userId) {
        this.preferenceRepository.deleteAllByUser(userId);
        this.watchService.deleteAllFromUserId(userId);
    }

    @Override
    public PreferenceInfoDTO getFilmInfo(Long userId, String filmId) {
        Optional<Preference> preferenceOptional = this.preferenceRepository.findOneByUserIdAndFilmId(userId, filmId);
        Optional<Watch> watchOptional = this.watchService.findOneByUserIdAndFilmId(userId, filmId);

        PreferenceInfoDTO preferenceInfoDTO = new PreferenceInfoDTO();

        preferenceInfoDTO.setIsInWatchlist(watchOptional.isPresent());
        preferenceOptional.ifPresent(preference -> preferenceInfoDTO.setMark(preference.getMark()));

        return preferenceInfoDTO;
    }

    @Override
    public List<PreferenceDTO> getPreferencesByUserAndFilms(Long userId, List<String> filmIds) {
        return this.preferenceMapper.toDTOList(this.preferenceRepository.findAllByUserIdAndFilms(userId, filmIds));
    }

}
