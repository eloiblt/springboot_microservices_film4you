package fr.cpe.filmforyou.userlib.dto;

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
public class UserLoginDTO implements Serializable {

    @NotNull(message = "authType doit être renseigné.")
    @NotBlank(message = "authType doit être renseigné.")
    private String authType;

    @NotNull(message = "token doit être renseigné.")
    @NotBlank(message = "token doit être renseigné.")
    private String token;

}
