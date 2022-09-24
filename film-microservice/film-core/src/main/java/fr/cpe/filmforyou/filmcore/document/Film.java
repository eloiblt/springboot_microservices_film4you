package fr.cpe.filmforyou.filmcore.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "films")
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Film {

    @Id
    private String id;

    @Field("imdb_title_id")
    private String imdbTitleId;

    @TextIndexed(weight = 3)
    private String title;

    private Long year;

    @Field("date_published")
    private LocalDate datePublished;

    private String genre;

    private Long duration;

    private String country;

    private String language;

    @TextIndexed
    private List<String> director;

    @TextIndexed
    private List<String> writer;

    @Field("production_company")
    private List<String> productionCompany;

    @TextIndexed
    private List<String> actors;

    @TextIndexed(weight = 2)
    private String description;

    @Field("avg_vote")
    private BigDecimal avgVote;

    private BigDecimal votes;

    private BigDecimal budget;

    private BigDecimal metascore;

    @Field("reviews_from_users")
    private BigDecimal reviewsFromUsers;

    @Field("reviews_from_critics")
    private BigDecimal reviewsFromCritics;

    @Field("img_url")
    private String imgUrl;

    private String currency;

    @Field("budget_euro")
    private BigDecimal budgetEuro;

}
