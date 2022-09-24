package fr.cpe.filmforyou.preferencecore.service;

import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.preferencelib.dto.*;

import java.util.List;


public interface PreferenceService {

    List<PreferenceDTO> findAllByUserId(Long userId);

    List<PreferenceFullDTO> findAllByUserIdFull(Long userId);

    PreferenceDTO createPreference(CreatePreferenceDTO createPreferenceDTO, Long userId);

    PreferenceDTO updatePreference(UpdatePreferenceDTO updatePreferenceDTO, Long userId) throws FilmForYouException;

    PreferenceDTO findByUserIdAndFilmId(Long userId, String filmId) throws FilmForYouException;

    PreferenceDTO deletePreference(Long userId, String filmId) throws FilmForYouException;

    List<String> findFilmMostLoved();

    PreferenceSummaryDTO getSummary(Long userId);

    void deleteAllFromUserId(Long userId);

    PreferenceInfoDTO getFilmInfo(Long userId, String filmId);

    List<PreferenceDTO> getPreferencesByUserAndFilms(Long userId, List<String> filmIds);

}
