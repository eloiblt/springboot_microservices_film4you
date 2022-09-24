package fr.cpe.filmforyou.preferencecore.service;

import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.preferencecore.bo.Watch;
import fr.cpe.filmforyou.preferencelib.dto.CreateWatchDTO;
import fr.cpe.filmforyou.preferencelib.dto.WatchDTO;
import fr.cpe.filmforyou.preferencelib.dto.WatchFullDTO;

import java.util.List;
import java.util.Optional;

public interface WatchService {

    WatchDTO add(CreateWatchDTO createWatchDTO, Long userId) throws FilmForYouException;

    WatchDTO remove(String filmId, Long userId) throws FilmForYouException;

    Boolean removeIfExist(String filmId, Long userId);

    List<WatchDTO> getByUser(Long userId);

    List<WatchFullDTO> getByUserFull(Long userId);

    void deleteAllFromUserId(Long userId);

    Optional<Watch> findOneByUserIdAndFilmId(Long userId, String filmId);

}
