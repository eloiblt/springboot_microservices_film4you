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
public class UserSearchDTO implements Serializable {

    private Long id;
    private String lastname;
    private String firstname;

}
