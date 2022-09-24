package fr.cpe.filmforyou.usercore.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.usercore.service.AuthService;
import fr.cpe.filmforyou.userlib.dto.UserAuthDTO;
import fr.cpe.filmforyou.userlib.dto.UserLoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleAuthServiceImpl implements AuthService {

    private final Logger logger = LoggerFactory.getLogger(GoogleAuthServiceImpl.class);
    @Value("${google.clientId}")
    private String clientId;

    @Override
    public UserAuthDTO getUser(UserLoginDTO userLoginDTO) throws FilmForYouException {
        this.logger.info("Collect user from token {}", userLoginDTO);

        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                    .setAudience(List.of(this.clientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(userLoginDTO.getToken());

            UserAuthDTO userAuthDTO = new UserAuthDTO();
            userAuthDTO.setEmail(googleIdToken.getPayload().getEmail());

            userAuthDTO.setFirstname((String) googleIdToken.getPayload().get("given_name"));
            userAuthDTO.setLastname((String) googleIdToken.getPayload().get("family_name"));

            return userAuthDTO;
        } catch (Exception e) {
            throw new FilmForYouException("Erreur lors de la récupération de l'utilisateur par Google");
        }
    }
}
