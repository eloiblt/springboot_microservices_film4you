package fr.cpe.filmforyou.filmcore.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmFavorite implements Serializable {

    @Id
    private String id;
    private String favoriteValue;

}
