package fr.cpe.filmforyou.userlib.dto;

import fr.cpe.filmforyou.preferencelib.dto.PreferenceFullDTO;
import fr.cpe.filmforyou.preferencelib.dto.WatchFullDTO;
import fr.cpe.filmforyou.userlib.enumeration.VisibilityEnum;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class UserFullDTO implements Serializable {

    private Long id;
    private String lastname;
    private String firstname;
    private String email;
    private VisibilityEnum visibility;
    private List<PreferenceFullDTO> preferences;
    private List<WatchFullDTO> watch;

}
