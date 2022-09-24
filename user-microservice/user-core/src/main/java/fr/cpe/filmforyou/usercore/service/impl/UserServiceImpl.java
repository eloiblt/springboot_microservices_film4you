package fr.cpe.filmforyou.usercore.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.preferencelib.dto.PreferenceSummaryDTO;
import fr.cpe.filmforyou.preferencelib.webservice.PreferenceWebService;
import fr.cpe.filmforyou.usercore.bo.User;
import fr.cpe.filmforyou.usercore.bo.UserSearchView;
import fr.cpe.filmforyou.usercore.mapper.UserMapper;
import fr.cpe.filmforyou.usercore.mapper.UserSearchMapper;
import fr.cpe.filmforyou.usercore.repository.UserRepository;
import fr.cpe.filmforyou.usercore.repository.UserSearchViewRepository;
import fr.cpe.filmforyou.usercore.service.TokenService;
import fr.cpe.filmforyou.usercore.service.UserService;
import fr.cpe.filmforyou.userlib.dto.*;
import fr.cpe.filmforyou.userlib.enumeration.VisibilityEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String USER_NOT_FOUND = "L'utilisateur d'id %d n'existe pas en base de données";

    private final UserRepository userRepository;
    private final UserSearchViewRepository userSearchViewRepository;
    private final GoogleAuthServiceImpl googleAuthService;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final UserSearchMapper userSearchMapper;
    private final PreferenceWebService preferenceWebService;

    public UserServiceImpl(UserRepository userRepository, UserSearchViewRepository userSearchViewRepository, GoogleAuthServiceImpl googleAuthService, TokenService tokenService, UserMapper userMapper, UserSearchMapper userSearchMapper, PreferenceWebService preferenceWebService) {
        this.userRepository = userRepository;
        this.userSearchViewRepository = userSearchViewRepository;
        this.googleAuthService = googleAuthService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.userSearchMapper = userSearchMapper;
        this.preferenceWebService = preferenceWebService;
    }

    @Override
    public String authUser(UserLoginDTO userLoginDTO) throws FilmForYouException {
        this.logger.info("Loggin user : {}", userLoginDTO);
        UserAuthDTO userAuthDTO = this.googleAuthService.getUser(userLoginDTO);

        Optional<User> userOptional = this.userRepository.findByEmail(userAuthDTO.getEmail());

        User user = userOptional.orElseGet(() -> {
            this.logger.info("Create user : {}", userAuthDTO);
            User createdUser = this.userMapper.toBo(userAuthDTO);
            createdUser.setVisibility(VisibilityEnum.PRIVATE);
            return this.userRepository.save(createdUser);
        });

        return tokenService.getTokenFromUser(this.userMapper.toDTO(user));
    }

    @Override
    public UserDTO getUserFromToken(String token) throws FilmForYouException {
        return this.tokenService.getUserFromToken(token);
    }

    @Override
    public Boolean isTokenValid(String token) {
        return this.tokenService.isTokenValid(token);
    }

    @Override
    public UserFullDTO getUserFull(Long userId, Boolean isPublic) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new FilmForYouException(String.format(USER_NOT_FOUND, userId), HttpStatus.NOT_FOUND));

        if (Boolean.TRUE.equals(isPublic) && VisibilityEnum.PRIVATE.equals(user.getVisibility())) {
            throw new FilmForYouException("Il n'est pas possible d'accéder au profil de cet utilisateur", HttpStatus.FORBIDDEN);
        }

        UserFullDTO userFullDTO = this.userMapper.toFullDTO(user);
        userFullDTO.setPreferences(this.preferenceWebService.findAllByUserFull(userId));
        userFullDTO.setWatch(this.preferenceWebService.findAllWatchByUserFull(userId));
        return userFullDTO;
    }

    @Override
    public UserSummaryDTO getUserSummary(Long userId, Boolean isPublic) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new FilmForYouException(String.format(USER_NOT_FOUND, userId), HttpStatus.NOT_FOUND));

        if (Boolean.TRUE.equals(isPublic) && VisibilityEnum.PRIVATE.equals(user.getVisibility())) {
            throw new FilmForYouException("Il n'est pas possible d'accéder au profil de cet utilisateur", HttpStatus.FORBIDDEN);
        }

        PreferenceSummaryDTO preferenceSummary = this.preferenceWebService.getSummary(userId);

        UserSummaryDTO userSummaryDTO = this.userMapper.toSummaryDTO(user);
        userSummaryDTO.setSummary(preferenceSummary);
        return userSummaryDTO;
    }

    @Override
    public UserDTO updateVisibility(VisibilityEnum visibilityEnum, Long userId) throws FilmForYouException {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new FilmForYouException(String.format(USER_NOT_FOUND, userId), HttpStatus.NOT_FOUND));
        user.setVisibility(visibilityEnum);

        return this.userMapper.toDTO(this.userRepository.save(user));
    }

    @Override
    public List<UserSearchDTO> search(String query) {
        ExampleMatcher caseMatch = ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<UserSearchView> example = Example.of(UserSearchView.builder().firstname(query).lastname(query).build(),
                caseMatch);

        return this.userSearchMapper.toDTOList(this.userSearchViewRepository.findAll(example, PageRequest.of(0, 5)).getContent());
    }

    @Override
    public UserDTO delete(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new FilmForYouException(String.format(USER_NOT_FOUND, userId), HttpStatus.NOT_FOUND));
        this.userRepository.delete(user);
        this.preferenceWebService.deletePreferences(userId);
        return this.userMapper.toDTO(user);
    }

    @Override
    public byte[] getUserData(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new FilmForYouException(String.format(USER_NOT_FOUND, userId), HttpStatus.NOT_FOUND));

        UserFullDTO userFullDTO = this.userMapper.toFullDTO(user);
        userFullDTO.setPreferences(this.preferenceWebService.findAllByUserFull(userId));
        userFullDTO.setWatch(this.preferenceWebService.findAllWatchByUserFull(userId));

        try {
            Path path = Files.createTempFile("user-data", ".json");
            Files.writeString(path, new ObjectMapper().writeValueAsString(userFullDTO));
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new FilmForYouException("Erreur lors de la génération du fichier");
        }
    }

}
