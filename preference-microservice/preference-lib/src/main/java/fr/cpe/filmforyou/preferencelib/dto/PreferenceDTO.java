package fr.cpe.filmforyou.preferencelib.dto;

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
public class PreferenceDTO implements Serializable {

    private Long id;
    private Long userId;
    private String filmId;
    private BigDecimal mark;

}
