package fr.cpe.filmforyou.preferencelib.dto;


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
public class PreferenceSummaryDTO implements Serializable {

    private Long filmViewed;
    private Long totalDuration;
    private String genre;
    private String director;
    private String production;
    private String actor;

}
