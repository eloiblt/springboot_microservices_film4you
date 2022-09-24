package fr.cpe.filmforyou.usercore.controller;

import fr.cpe.filmforyou.config.annotation.ReadOnlyTransaction;
import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.usercore.service.UserService;
import fr.cpe.filmforyou.userlib.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/public/login")
    @Operation(description = "Authentifie un utilisateur par rapport au token généré par Google")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Le body de la requête n'est pas correct"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de l'authentification avec Google")
    })
    public String loginUser(@RequestBody @Valid final UserLoginDTO userLoginDTO) {
        return this.userService.authUser(userLoginDTO);
    }

    @PostMapping("/private/auth/user")
    @Operation(description = "Transmet le user dto présent dans le token JWT si ce dernier est correct")
    @ApiResponse(responseCode = "500", description = "Le token n'a pas pu être parsé.")
    @Cacheable("getUser")
    public UserDTO getUser(@RequestBody final String token) throws FilmForYouException {
        return this.userService.getUserFromToken(token);
    }

    @GetMapping(value = "/secured/current")
    @Operation(description = "Retourne l'utilisateur courant depuis une requête sécurisé", security = @SecurityRequirement(name = "auth-bearer"))
    public UserDTO getCurrent(@AuthenticationPrincipal final UserDTO userDTO) {
        return userDTO;
    }

    @PostMapping(value = "/private/token/valid")
    @Operation(description = "Retourne l'utilisateur courant depuis une requête privé")
    @Cacheable("isTokenValid")
    public Boolean isTokenValid(@RequestBody final String token) {
        return this.userService.isTokenValid(token);
    }

    @GetMapping(value = "/secured/full")
    @Operation(description = "Retourne l'utilisateur courant avec sa liste de préférence", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas en base de données")
    @ReadOnlyTransaction
    public UserFullDTO getCurrentUserFull(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.userService.getUserFull(userDTO.getId(), Boolean.FALSE);
    }

    @GetMapping(value = "/secured/summary")
    @Operation(description = "Retourne l'utilisateur courant avec ses statistiques", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas en base de données")
    @ReadOnlyTransaction
    public UserSummaryDTO getCurrentUserSummary(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.userService.getUserSummary(userDTO.getId(), Boolean.FALSE);
    }

    @GetMapping(value = "/public/{userId}/full")
    @Operation(description = "Retourne un utilisateur avec sa liste de préférence")
    @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas en base de données")
    @ReadOnlyTransaction
    public UserFullDTO getUserFull(@PathVariable(name = "userId") final Long userId) {
        return this.userService.getUserFull(userId, Boolean.TRUE);
    }

    @GetMapping(value = "/public/{userId}/summary")
    @Operation(description = "Retourne un utilisateur avec ses statistiques")
    @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas en base de données")
    @ReadOnlyTransaction
    public UserSummaryDTO getUserSummary(@PathVariable(name = "userId") final Long userId) {
        return this.userService.getUserSummary(userId, Boolean.TRUE);
    }

    @GetMapping(value = "/secured/data", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(description = "Retourne un fichier json avec les données de l'utilisateur", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas en base de données")
    @ReadOnlyTransaction
    public byte[] getUserData(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.userService.getUserData(userDTO.getId());
    }

    @PostMapping(value = "/secured/visibility")
    @Operation(description = "Mets à jour la visibilité de l'utilisateur", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas en base de données")
    public UserDTO updateVisibility(@RequestBody final UserVisibilityDTO userVisibilityDTO, @AuthenticationPrincipal final UserDTO userDTO) {
        return this.userService.updateVisibility(userVisibilityDTO.getVisibility(), userDTO.getId());
    }

    @GetMapping(value = "/secured/search")
    @Operation(description = "Permet de rechercher un utilisateur avec le profil public", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas en base de données")
    @ReadOnlyTransaction
    public List<UserSearchDTO> searchUser(@RequestParam(name = "query", required = true) String query) {
        return this.userService.search(query);
    }

    @DeleteMapping(value = "/secured/delete")
    @Operation(description = "Suppression d'un utilisateur et de ses préférences", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas en base de données")
    public UserDTO deleteUser(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.userService.delete(userDTO.getId());
    }

}
