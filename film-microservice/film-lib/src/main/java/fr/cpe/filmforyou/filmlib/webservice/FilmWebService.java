package fr.cpe.filmforyou.filmlib.webservice;

import fr.cpe.filmforyou.filmlib.dto.FilmDTO;
import fr.cpe.filmforyou.filmlib.dto.FilmSummaryDTO;
import fr.cpe.filmforyou.filmlib.dto.FilmSummaryRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("films")
public interface FilmWebService {

    @PostMapping("/private/list")
    List<FilmDTO> findAllWithIds(List<String> ids);

    @GetMapping("/private/get-all")
    List<FilmDTO> getALl();

    @PostMapping("/private/summary")
    FilmSummaryDTO getSummary(@RequestBody FilmSummaryRequestDTO filmSummaryRequestDTO);

    @GetMapping("/private/exist/{id}")
    Boolean exist(@PathVariable(value = "id", required = true) final String id);

}
