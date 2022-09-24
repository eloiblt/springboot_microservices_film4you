package fr.cpe.filmforyou.preferencecore.mapper;

import fr.cpe.filmforyou.preferencecore.bo.Watch;
import fr.cpe.filmforyou.preferencelib.dto.WatchDTO;
import fr.cpe.filmforyou.preferencelib.dto.WatchFullDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface WatchMapper {

    Watch toBo(WatchDTO source);

    WatchDTO toDTO(Watch source);

    List<WatchDTO> toDTOList(List<Watch> sources);

    List<Watch> toBoList(List<WatchDTO> sources);

    WatchFullDTO toFullDTO(Watch source);

}
