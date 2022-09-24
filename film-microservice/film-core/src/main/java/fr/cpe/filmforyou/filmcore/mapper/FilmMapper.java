package fr.cpe.filmforyou.filmcore.mapper;

import fr.cpe.filmforyou.filmcore.document.Film;
import fr.cpe.filmforyou.filmcore.document.FilmLight;
import fr.cpe.filmforyou.filmcore.document.FilmSearch;
import fr.cpe.filmforyou.filmlib.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface FilmMapper {

    FilmDTO toDTO(Film source);

    List<FilmDTO> toDTOList(List<Film> sources);

    List<FilmDTO> toDTOList(Iterable<Film> sources);

    @Mapping(target = "userNote", ignore = true)
    FilmLightDTO toLightDTO(Film source);

    List<FilmLightDTO> toLightDTOList(List<Film> sources);

    List<FilmLightDTO> toLightDTOList(Iterable<Film> sources);

    @Mapping(target = "userNote", ignore = true)
    FilmLightDTO toLightFromLight(FilmLight source);

    List<FilmLightDTO> toLightFromLightList(List<FilmLight> source);

    FilmSearchResponseDTO toFilmSearchResponse(FilmSearch source);

    List<FilmSearchResponseDTO> toFilmSearchResponseList(List<FilmSearch> sources);

}
