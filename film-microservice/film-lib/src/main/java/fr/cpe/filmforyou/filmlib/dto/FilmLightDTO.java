package fr.cpe.filmforyou.filmlib.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FilmLightDTO implements Serializable {

    private String id;
    private String title;
    private BigDecimal avgVote;
    private String imgUrl;
    private BigDecimal userNote;

}
