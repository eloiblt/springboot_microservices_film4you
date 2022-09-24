package fr.cpe.filmforyou.filmlib.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FilmDTO implements Serializable {

    private String id;
    private String imdbTitleId;
    private String title;
    private Long year;
    private LocalDate datePublished;
    private String genre;
    private Long duration;
    private String country;
    private String language;
    private List<String> director;
    private List<String> writer;
    private List<String> productionCompany;
    private List<String> actors;
    private String description;
    private BigDecimal avgVote;
    private BigDecimal votes;
    private BigDecimal budget;
    private BigDecimal metascore;
    private BigDecimal reviewsFromUsers;
    private BigDecimal reviewsFromCritics;
    private String imgUrl;
    private String currency;
    private BigDecimal budgetEuro;

}
