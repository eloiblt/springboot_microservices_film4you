package fr.cpe.filmforyou.preferencecore.mapper;

import fr.cpe.filmforyou.preferencecore.bo.Preference;
import fr.cpe.filmforyou.preferencelib.dto.CreatePreferenceDTO;
import fr.cpe.filmforyou.preferencelib.dto.PreferenceDTO;
import fr.cpe.filmforyou.preferencelib.dto.PreferenceFullDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface PreferenceMapper {

    Preference toBo(PreferenceDTO source);

    PreferenceDTO toDTO(Preference source);

    List<PreferenceDTO> toDTOList(List<Preference> sources);

    List<Preference> toBoList(List<PreferenceDTO> sources);

    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "userId", ignore = true)
    })
    Preference toBo(CreatePreferenceDTO source);

    @Mappings(value = {
            @Mapping(target = "film", ignore = true)
    })
    PreferenceFullDTO toFullDTO(Preference preference);
}
