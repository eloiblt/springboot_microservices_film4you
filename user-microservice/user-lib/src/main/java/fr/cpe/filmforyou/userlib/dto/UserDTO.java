package fr.cpe.filmforyou.userlib.dto;

import fr.cpe.filmforyou.userlib.enumeration.VisibilityEnum;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class UserDTO implements Serializable {

    private Long id;
    private String lastname;
    private String firstname;
    private String email;
    private VisibilityEnum visibility;

}