package fr.cpe.filmforyou.preferencelib.dto;

import fr.cpe.filmforyou.filmlib.dto.FilmDTO;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class WatchFullDTO implements Serializable {

    private Long id;
    private Long userId;
    private FilmDTO film;

}
