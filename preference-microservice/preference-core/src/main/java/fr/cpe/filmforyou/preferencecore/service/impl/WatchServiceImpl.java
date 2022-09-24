package fr.cpe.filmforyou.preferencecore.service.impl;

import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.filmlib.dto.FilmDTO;
import fr.cpe.filmforyou.filmlib.webservice.FilmWebService;
import fr.cpe.filmforyou.preferencecore.bo.Watch;
import fr.cpe.filmforyou.preferencecore.mapper.WatchMapper;
import fr.cpe.filmforyou.preferencecore.repository.WatchRepository;
import fr.cpe.filmforyou.preferencecore.service.WatchService;
import fr.cpe.filmforyou.preferencelib.dto.CreateWatchDTO;
import fr.cpe.filmforyou.preferencelib.dto.WatchDTO;
import fr.cpe.filmforyou.preferencelib.dto.WatchFullDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchServiceImpl implements WatchService {

    private final WatchRepository watchRepository;
    private final WatchMapper watchMapper;
    private final FilmWebService filmWebService;

    public WatchServiceImpl(WatchRepository watchRepository, WatchMapper watchMapper, FilmWebService filmWebService) {
        this.watchRepository = watchRepository;
        this.watchMapper = watchMapper;
        this.filmWebService = filmWebService;
    }

    @Override
    public WatchDTO add(CreateWatchDTO createWatchDTO, Long userId) throws FilmForYouException {
        Optional<Watch> watchOptional = this.watchRepository.exists(createWatchDTO.getFilmId(), userId);

        watchOptional.ifPresent(val -> {
            throw new FilmForYouException("Vous avez déjà ajouté ce film dans votre liste", HttpStatus.BAD_REQUEST);
        });

        if (Boolean.FALSE.equals(this.filmWebService.exist(createWatchDTO.getFilmId()))) {
            throw new FilmForYouException("Ce film n'existe pas en base de données", HttpStatus.BAD_REQUEST);
        }

        Watch watch = Watch.builder()
                .userId(userId)
                .filmId(createWatchDTO.getFilmId())
                .build();

        return this.watchMapper.toDTO(this.watchRepository.save(watch));
    }

    @Override
    @Transactional
    public WatchDTO remove(String filmId, Long userId) throws FilmForYouException {
        Optional<Watch> watchOptional = this.watchRepository.exists(filmId, userId);

        if (watchOptional.isEmpty()) {
            throw new FilmForYouException("Vous ne possedez pas ce film dans votre liste", HttpStatus.BAD_REQUEST);
        }

        this.watchRepository.deleteByUserIdAndFilmId(filmId, userId);

        return this.watchMapper.toDTO(watchOptional.get());
    }

    @Override
    public Boolean removeIfExist(String filmId, Long userId) {
        Optional<Watch> watchOptional = this.watchRepository.exists(filmId, userId);
        watchOptional.ifPresent(watch -> this.watchRepository.deleteByUserIdAndFilmId(filmId, userId));
        return watchOptional.isPresent();
    }

    @Override
    public List<WatchDTO> getByUser(Long userId) {
        return this.watchMapper.toDTOList(this.watchRepository.findAllByUser(userId));
    }

    @Override
    public List<WatchFullDTO> getByUserFull(Long userId) {
        List<Watch> watchList = this.watchRepository.findAllByUser(userId);

        if (!watchList.isEmpty()) {
            List<FilmDTO> filmList = this.filmWebService.findAllWithIds(watchList.stream().map(Watch::getFilmId).collect(Collectors.toList()));

            List<WatchFullDTO> watchFullDTOList = new ArrayList<>(watchList.size());

            watchList.forEach(watch -> {

                WatchFullDTO watchFullDTO = this.watchMapper.toFullDTO(watch);
                filmList.stream().filter(filmDTO -> filmDTO.getId().equals(watch.getFilmId())).findFirst().ifPresent(watchFullDTO::setFilm);
                watchFullDTOList.add(watchFullDTO);
            });

            return watchFullDTOList;
        }

        return new ArrayList<>(0);
    }

    @Override
    public void deleteAllFromUserId(Long userId) {
        this.watchRepository.deleteAllByUser(userId);
    }

    @Override
    public Optional<Watch> findOneByUserIdAndFilmId(Long userId, String filmId) {
        return this.watchRepository.exists(filmId, userId);
    }
}
