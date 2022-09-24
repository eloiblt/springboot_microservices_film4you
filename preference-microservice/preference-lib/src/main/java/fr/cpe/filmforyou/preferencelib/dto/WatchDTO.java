package fr.cpe.filmforyou.preferencelib.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class WatchDTO implements Serializable {

    private Long id;
    private Long userId;
    private String filmId;

}
