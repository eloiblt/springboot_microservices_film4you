package fr.cpe.filmforyou.usercore.mapper;

import fr.cpe.filmforyou.usercore.bo.User;
import fr.cpe.filmforyou.userlib.dto.UserAuthDTO;
import fr.cpe.filmforyou.userlib.dto.UserDTO;
import fr.cpe.filmforyou.userlib.dto.UserFullDTO;
import fr.cpe.filmforyou.userlib.dto.UserSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toBo(UserAuthDTO userAuthDTO);

    UserDTO toDTO(User source);

    @Mapping(target = "preferences", ignore = true)
    UserFullDTO toFullDTO(User source);

    @Mapping(target = "summary", ignore = true)
    UserSummaryDTO toSummaryDTO(User source);

    List<UserDTO> toDTOList(List<User> sources);

}
