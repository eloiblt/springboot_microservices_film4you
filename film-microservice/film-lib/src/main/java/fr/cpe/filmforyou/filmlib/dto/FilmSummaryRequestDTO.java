package fr.cpe.filmforyou.filmlib.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FilmSummaryRequestDTO implements Serializable {

    private List<String> filmsLiked;
    private List<String> filmsViewed;

}
