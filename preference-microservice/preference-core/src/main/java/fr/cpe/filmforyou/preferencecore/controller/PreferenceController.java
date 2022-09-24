package fr.cpe.filmforyou.preferencecore.controller;

import fr.cpe.filmforyou.config.annotation.ReadOnlyTransaction;
import fr.cpe.filmforyou.exception.FilmForYouException;
import fr.cpe.filmforyou.preferencecore.service.PreferenceService;
import fr.cpe.filmforyou.preferencelib.dto.*;
import fr.cpe.filmforyou.userlib.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @GetMapping("/secured/user")
    @ReadOnlyTransaction
    @Operation(description = "Retourne toutes les préférences de l'utilisateur courant", security = @SecurityRequirement(name = "auth-bearer"))
    public List<PreferenceDTO> findAllByUser(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.preferenceService.findAllByUserId(userDTO.getId());
    }

    @GetMapping("/private/user/{userId}")
    @ReadOnlyTransaction
    @Operation(description = "Retourne toutes les préférences de l'utilisateur passé en paramètres")
    public List<PreferenceDTO> findAllByUserPrivate(@PathVariable(name = "userId", required = true) final Long userId) {
        return this.preferenceService.findAllByUserId(userId);
    }

    @GetMapping("/secured/user/full")
    @ReadOnlyTransaction
    @Operation(description = "Retourne toutes les préférences de l'utilisateur courant avec les films", security = @SecurityRequirement(name = "auth-bearer"))
    public List<PreferenceFullDTO> findAllByUserFull(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.preferenceService.findAllByUserIdFull(userDTO.getId());
    }

    @GetMapping("/private/user/{userId}/full")
    @ReadOnlyTransaction
    @Operation(description = "Retourne toutes les préférences de l'utilisateur courant avec les films")
    public List<PreferenceFullDTO> findAllByUserFullPrivate(@PathVariable(name = "userId", required = true) final Long userId) {
        return this.preferenceService.findAllByUserIdFull(userId);
    }

    @GetMapping("/secured/mark/{filmId}")
    @ReadOnlyTransaction
    @Operation(description = "Retourne une préférence pour un film et l'utilisateur courant", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Il n'y a pas de note pour cet utilisateur et ce film."),
            @ApiResponse(responseCode = "200", description = "Retourne une préférence pour un film et l'utilisateur courant")
    })
    public PreferenceDTO findByUserIdAndFilmId(@AuthenticationPrincipal final UserDTO userDTO, @PathVariable(name = "filmId", required = true) final String filmId) {
        return this.preferenceService.findByUserIdAndFilmId(userDTO.getId(), filmId);
    }

    @PostMapping("/secured/mark")
    @Operation(description = "Permet de créer une nouvelle préférence.", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Il existe déjà une note pour ce film"),
            @ApiResponse(responseCode = "400", description = "Le body de la requête n'est pas correct"),
            @ApiResponse(responseCode = "400", description = "La note n'est pas entre 0 et 10."),
            @ApiResponse(responseCode = "400", description = "Le film n'existe pas")
    })
    public PreferenceDTO createPreference(@RequestBody @Valid final CreatePreferenceDTO createPreferenceDTO, @AuthenticationPrincipal final UserDTO userDTO) throws FilmForYouException {
        return this.preferenceService.createPreference(createPreferenceDTO, userDTO.getId());
    }

    @PutMapping("/secured/mark")
    @Operation(description = "Permet de mettre à jour une préférence.", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Le body de la requête n'est pas correct"),
            @ApiResponse(responseCode = "400", description = "La note n'est pas entre 0 et 10."),
            @ApiResponse(responseCode = "400", description = "Il n'y a pas de note pour ce film")
    })
    public PreferenceDTO updatePreference(@RequestBody @Valid final UpdatePreferenceDTO updatePreferenceDTO, @AuthenticationPrincipal final UserDTO userDTO) {
        return this.preferenceService.updatePreference(updatePreferenceDTO, userDTO.getId());
    }

    @DeleteMapping("/secured/mark/{filmId}")
    @Operation(description = "Supprime une préférence d'un utilisateur.", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponse(responseCode = "404", description = "Il n'y a pas de note pour cet utilisateur et ce film.")
    public PreferenceDTO deletePreference(@AuthenticationPrincipal final UserDTO userDTO, @PathVariable(name = "filmId", required = true) final String filmId) {
        return this.preferenceService.deletePreference(userDTO.getId(), filmId);
    }

    @GetMapping("/private/film/most/loved")
    @Operation(description = "Retourne les id de film les plus aimés")
    public List<String> getFilmMostLoved() {
        return this.preferenceService.findFilmMostLoved();
    }

    @GetMapping("/private/sumary/{userId}")
    @Operation(description = "Retourne les statistiques d'un utilisateur")
    @ReadOnlyTransaction
    public PreferenceSummaryDTO getSummary(@PathVariable(name = "userId", required = true) final Long userId) {
        return this.preferenceService.getSummary(userId);
    }

    @DeleteMapping("/private/delete/user/{userId}")
    @Operation(description = "Suppression des préférences d'un utilisateur")
    public void deletePreferences(@PathVariable(name = "userId", required = true) final Long userId) {
        this.preferenceService.deleteAllFromUserId(userId);
    }

    @GetMapping("/secured/{filmId}/info")
    @Operation(description = "Retourne les informations de l'utilisateur en lien avec le film", security = @SecurityRequirement(name = "auth-bearer"))
    @ReadOnlyTransaction
    public PreferenceInfoDTO getFilmInfo(@AuthenticationPrincipal final UserDTO userDTO, @PathVariable(name = "filmId", required = true) final String filmId) {
        return this.preferenceService.getFilmInfo(userDTO.getId(), filmId);
    }

    @PostMapping("/private/{userId}")
    @Operation(description = "Retourne les préférences d'un utilisateur pour des films Ids")
    @ReadOnlyTransaction
    public List<PreferenceDTO> getPreferencesByFilmIdsAndUser(@PathVariable(name = "userId") final Long userId, @RequestBody List<String> filmIds) {
        return this.preferenceService.getPreferencesByUserAndFilms(userId, filmIds);
    }


}
