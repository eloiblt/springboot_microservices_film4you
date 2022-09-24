package fr.cpe.filmforyou.usercore.mapper;

import fr.cpe.filmforyou.usercore.bo.UserSearchView;
import fr.cpe.filmforyou.userlib.dto.UserSearchDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface UserSearchMapper {

    UserSearchDTO toDTO(UserSearchView source);

    List<UserSearchDTO> toDTOList(List<UserSearchView> sources);

}
