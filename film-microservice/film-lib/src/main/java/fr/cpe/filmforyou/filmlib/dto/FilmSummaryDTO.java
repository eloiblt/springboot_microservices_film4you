package fr.cpe.filmforyou.filmlib.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Data
public class FilmSummaryDTO implements Serializable {

    private Long totalDuration;
    private String genre;
    private String director;
    private String production;
    private String actor;

}
