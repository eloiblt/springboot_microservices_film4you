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
public class UserVisibilityDTO implements Serializable {

    private VisibilityEnum visibility;

}
