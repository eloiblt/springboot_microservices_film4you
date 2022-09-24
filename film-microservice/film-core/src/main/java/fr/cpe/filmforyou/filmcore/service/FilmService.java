package fr.cpe.filmforyou.filmcore.service;

import fr.cpe.filmforyou.filmlib.dto.*;

import java.util.List;

public interface FilmService {

    List<FilmLightDTO> findAllInList(List<String> ids);

    FilmDTO findById(String filmId);

    List<FilmLightDTO> getReleased(Long userId);

    List<FilmLightDTO> getMostLoved(Long userId);

    List<FilmLightDTO> getPopular(Long userId);

    List<FilmDTO> getAll();

    List<FilmSearchResponseDTO> search(String searchQuery, FilmSearchRequestDTO filmSearchRequestDTO);

    Boolean exists(String filmId);

    FilmSummaryDTO getSummary(FilmSummaryRequestDTO filmSummaryRequestDTO);

    List<String> getGenres();
}
