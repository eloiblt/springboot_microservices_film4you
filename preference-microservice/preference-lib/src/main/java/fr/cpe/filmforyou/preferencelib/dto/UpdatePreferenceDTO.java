package fr.cpe.filmforyou.preferencelib.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class UpdatePreferenceDTO implements Serializable {

    @NotNull(message = "filmId doit être renseigné.")
    @NotBlank(message = "filmId doit être renseigné.")
    private String filmId;

    @NotNull(message = "mark doit être renseigné.")
    @Max(value = 11, message = "La note ne peut pas être supérieur à 10")
    @Min(value = -1, message = "La note ne peut pas être inférieur à 0")
    private BigDecimal mark;

}
