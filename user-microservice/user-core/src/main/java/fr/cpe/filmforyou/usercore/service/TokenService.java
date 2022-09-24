package fr.cpe.filmforyou.usercore.service;

import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.userlib.dto.UserDTO;

public interface TokenService {

    String getTokenFromUser(UserDTO userDTO) throws FilmForYouException;

    UserDTO getUserFromToken(String token) throws FilmForYouException;

    Boolean isTokenValid(String token);

}
