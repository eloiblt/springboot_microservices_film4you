package fr.cpe.filmforyou.filmcore.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "films")
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FilmSearch {

    @Id
    private String id;

    @TextIndexed(weight = 3)
    private String title;

    private Long year;

    private String genre;

    @TextIndexed
    private List<String> director;

    @TextIndexed
    private List<String> writer;

    @TextIndexed
    private List<String> actors;

    @TextIndexed(weight = 2)
    private String description;

    @Field("img_url")
    private String imgUrl;

}
