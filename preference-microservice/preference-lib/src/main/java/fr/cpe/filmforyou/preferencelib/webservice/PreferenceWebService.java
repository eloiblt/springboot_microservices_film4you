package fr.cpe.filmforyou.preferencelib.webservice;

import fr.cpe.filmforyou.preferencelib.dto.PreferenceDTO;
import fr.cpe.filmforyou.preferencelib.dto.PreferenceFullDTO;
import fr.cpe.filmforyou.preferencelib.dto.PreferenceSummaryDTO;
import fr.cpe.filmforyou.preferencelib.dto.WatchFullDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("preferences")
public interface PreferenceWebService {

    @GetMapping("/private/user/{userId}")
    List<PreferenceDTO> findAllByUserPrivate(@PathVariable(name = "userId", required = true) final Long userId);

    @GetMapping("/private/user/{userId}/full")
    List<PreferenceFullDTO> findAllByUserFull(@PathVariable(name = "userId") final Long userId);

    @GetMapping("/private/film/most/loved")
    List<String> getMostLoved();

    @GetMapping("/private/sumary/{userId}")
    PreferenceSummaryDTO getSummary(@PathVariable(name = "userId") final Long userId);

    @DeleteMapping("/private/delete/user/{userId}")
    void deletePreferences(@PathVariable(name = "userId") final Long userId);

    @PostMapping("/private/{userId}")
    List<PreferenceDTO> getPreferencesByFilmIdsAndUser(@PathVariable(name = "userId") final Long userId, @RequestBody List<String> filmIds);

    @GetMapping("/private/watch/{userId}/full")
    List<WatchFullDTO> findAllWatchByUserFull(@PathVariable(name = "userId") final Long userId);

}
