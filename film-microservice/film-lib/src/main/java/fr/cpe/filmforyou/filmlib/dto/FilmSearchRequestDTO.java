package fr.cpe.filmforyou.filmlib.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FilmSearchRequestDTO implements Serializable {

    private String genre;
    private Long year;

}
