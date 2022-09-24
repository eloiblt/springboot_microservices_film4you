package fr.cpe.filmforyou.filmcore.document;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.math.BigDecimal;

@Document(collection = "films")
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FilmLight {

    @Id
    private String id;

    private String title;

    @Field("avg_vote")
    private BigDecimal avgVote;

    @Field("img_url")
    private String imgUrl;

}
