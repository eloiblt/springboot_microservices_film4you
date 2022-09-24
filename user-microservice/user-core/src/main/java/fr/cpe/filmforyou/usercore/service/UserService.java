package fr.cpe.filmforyou.usercore.service;

import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.userlib.dto.*;
import fr.cpe.filmforyou.userlib.enumeration.VisibilityEnum;

import java.util.List;

public interface UserService {

    String authUser(UserLoginDTO userLoginDTO);

    UserDTO getUserFromToken(String token);

    Boolean isTokenValid(String token);

    UserFullDTO getUserFull(Long userId, Boolean isPublic);

    UserSummaryDTO getUserSummary(Long userId, Boolean isPublic);

    UserDTO updateVisibility(VisibilityEnum visibilityEnum, Long userId) throws FilmForYouException;

    List<UserSearchDTO> search(String query);

    UserDTO delete(Long userId);

    byte[] getUserData(Long userId);

}
