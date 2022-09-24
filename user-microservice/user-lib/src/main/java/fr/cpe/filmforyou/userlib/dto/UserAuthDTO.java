package fr.cpe.filmforyou.userlib.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class UserAuthDTO implements Serializable {

    private String lastname;
    private String firstname;
    private String email;

}
