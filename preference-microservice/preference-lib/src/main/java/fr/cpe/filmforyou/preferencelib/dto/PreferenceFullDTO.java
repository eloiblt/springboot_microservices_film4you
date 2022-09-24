package fr.cpe.filmforyou.preferencelib.dto;

import fr.cpe.filmforyou.filmlib.dto.FilmDTO;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class PreferenceFullDTO implements Serializable {

    private Long id;
    private Long userId;
    private FilmDTO film;
    private BigDecimal mark;

}
