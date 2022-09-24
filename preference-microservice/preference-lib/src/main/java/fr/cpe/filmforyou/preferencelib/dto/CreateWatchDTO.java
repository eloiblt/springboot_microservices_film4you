package fr.cpe.filmforyou.preferencelib.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class CreateWatchDTO implements Serializable {

    @NotNull(message = "filmId doit être renseigné.")
    @NotBlank(message = "filmId doit être renseigné.")
    private String filmId;

}
