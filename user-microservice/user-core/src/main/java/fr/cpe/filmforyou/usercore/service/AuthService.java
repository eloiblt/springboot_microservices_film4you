package fr.cpe.filmforyou.usercore.service;

import fr.cpe.filmforyou.userlib.dto.UserAuthDTO;
import fr.cpe.filmforyou.userlib.dto.UserLoginDTO;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthService {

    UserAuthDTO getUser(UserLoginDTO userLoginDTO) throws GeneralSecurityException, IOException;

}
