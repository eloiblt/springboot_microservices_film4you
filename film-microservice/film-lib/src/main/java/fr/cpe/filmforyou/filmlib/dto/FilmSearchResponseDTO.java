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
public class FilmSearchResponseDTO implements Serializable {

    private String id;
    private String title;
    private Long year;
    private String genre;
    private List<String> actors;
    private String imgUrl;

}
