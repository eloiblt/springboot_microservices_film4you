package fr.cpe.filmforyou.filmcore.controller;

import fr.cpe.filmforyou.config.annotation.ReadOnlyTransaction;
import fr.cpe.filmforyou.filmcore.service.FilmService;
import fr.cpe.filmforyou.filmlib.dto.*;
import fr.cpe.filmforyou.userlib.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/private/list")
    @ReadOnlyTransaction
    @Operation(description = "Retourne tous les films avec un id présent dans la liste")
    @ApiResponse(responseCode = "200")
    public List<FilmLightDTO> findAllWithIds(@RequestBody List<String> ids) {
        return this.filmService.findAllInList(ids);
    }

    @GetMapping("/secured/{filmId}")
    @ReadOnlyTransaction
    @Operation(description = "Retourne un film par son Id", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Récupération du film"),
            @ApiResponse(responseCode = "404", description = "Le film n'existe pas")
    })
    public FilmDTO findById(@PathVariable(name = "filmId") String filmId) {
        return this.filmService.findById(filmId);
    }

    @GetMapping("/secured/released")
    @ReadOnlyTransaction
    @Operation(description = "Retourne les dernières sorties", security = @SecurityRequirement(name = "auth-bearer"))
    @ApiResponse(responseCode = "200")
    public List<FilmLightDTO> getReleased(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.filmService.getReleased(userDTO.getId());
    }

    @GetMapping("/secured/most/loved")
    @ReadOnlyTransaction
    @Operation(description = "Retourne les films avec les meilleurs note", security = @SecurityRequirement(name = "auth-bearer"))
    public List<FilmLightDTO> getMostLoved(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.filmService.getMostLoved(userDTO.getId());
    }

    @GetMapping("/secured/popular")
    @ReadOnlyTransaction
    @Operation(description = "Retourne les films les plus populaire", security = @SecurityRequirement(name = "auth-bearer"))
    public List<FilmLightDTO> getPopular(@AuthenticationPrincipal final UserDTO userDTO) {
        return this.filmService.getPopular(userDTO.getId());
    }

    @GetMapping("/private/get-all")
    @ReadOnlyTransaction
    @Operation(description = "Retourne tous les films présents dans la base de données")
    public List<FilmDTO> getALl() {
        return this.filmService.getAll();
    }


    @PostMapping("/secured/search")
    @ReadOnlyTransaction
    @Operation(description = "Retourne une liste de 5 éléments maximum en lien avec le paramètre query", security = @SecurityRequirement(name = "auth-bearer"))
    public List<FilmSearchResponseDTO> search(@RequestParam(name = "query", required = true) final String searchQuery, @RequestBody final FilmSearchRequestDTO filmSearchRequestDTO) {
        return this.filmService.search(searchQuery, filmSearchRequestDTO);
    }

    @PostMapping("/private/summary")
    @ReadOnlyTransaction
    @Operation(description = "Retourne une résumé des préférences de l'utilisateur en fonction de ses films aimés et ses films vus.")
    public FilmSummaryDTO getSummary(@RequestBody FilmSummaryRequestDTO filmSummaryRequestDTO) {
        return this.filmService.getSummary(filmSummaryRequestDTO);
    }

    @GetMapping("/private/exist/{id}")
    @ReadOnlyTransaction
    @Operation(description = "Retourne true si jamais un film existe dans la base, false dans le cas contraire")
    public Boolean exist(@PathVariable(value = "id", required = true) final String id) {
        return this.filmService.exists(id);
    }

    @GetMapping("/secured/genres")
    @ReadOnlyTransaction
    @Operation(description = "Retourne la liste des genres", security = @SecurityRequirement(name = "auth-bearer"))
    public List<String> getGenres() {
        return this.filmService.getGenres();
    }
}
