package fr.cpe.filmforyou.preferencecore.controller;

import fr.cpe.filmforyou.config.annotation.ReadOnlyTransaction;
import fr.cpe.filmforyou.preferencecore.service.WatchService;
import fr.cpe.filmforyou.preferencelib.dto.CreateWatchDTO;
import fr.cpe.filmforyou.preferencelib.dto.WatchDTO;
import fr.cpe.filmforyou.preferencelib.dto.WatchFullDTO;
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
public class WatchController {

    private final WatchService watchService;

    public WatchController(WatchService watchService) {
        this.watchService = watchService;
    }

    @GetMapping("/secured/watch")
    @ReadOnlyTransaction
    @Operation(description = "Retourne tous les films que l'utilisateur à prévu de regarder", security = @SecurityRequirement(name = "auth-bearer"))
    public List<WatchDTO> getPlanToWatch(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.watchService.getByUser(userDTO.getId());
    }

    @GetMapping("/secured/watch/full")
    @ReadOnlyTransaction
    @Operation(description = "Retourne tous les films que l'utilisateur à prévu de regarder avec le contenu des films", security = @SecurityRequirement(name = "auth-bearer"))
    public List<WatchFullDTO> getPlanToWatchFull(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.watchService.getByUserFull(userDTO.getId());
    }

    @GetMapping("/private/watch/{userId}/full")
    @ReadOnlyTransaction
    @Operation(description = "Retourne tous les films que l'utilisateur à prévu de regarder")
    public List<WatchFullDTO> getPlanToWatchFull(@PathVariable final Long userId) {
        return this.watchService.getByUserFull(userId);
    }

    @PostMapping("/secured/watch")
    @Operation(description = "Ajoute un film à dans la liste à regarder", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Ce film est déjà dans la watchlist"),
            @ApiResponse(responseCode = "400", description = "Le body de la requête n'est pas correct"),
            @ApiResponse(responseCode = "400", description = "Le film n'existe pas")
    })
    public WatchDTO addToWatch(@RequestBody @Valid final CreateWatchDTO createWatchDTO, @AuthenticationPrincipal final UserDTO userDTO) {
        return this.watchService.add(createWatchDTO, userDTO.getId());
    }

    @DeleteMapping("/secured/watch/{id}")
    @Operation(description = "Supprimer un film de la liste à regarder", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponse(responseCode = "400", description = "Ce film n'est déjà dans la watchlist")
    public WatchDTO deleteToWatch(@PathVariable final String id, @AuthenticationPrincipal final UserDTO userDTO) {
        return this.watchService.remove(id, userDTO.getId());
    }

}
