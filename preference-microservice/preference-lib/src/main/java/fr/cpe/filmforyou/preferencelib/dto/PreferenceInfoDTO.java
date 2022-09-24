package fr.cpe.filmforyou.preferencelib.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class PreferenceInfoDTO implements Serializable {

    private Boolean isInWatchlist;
    private BigDecimal mark;

}
