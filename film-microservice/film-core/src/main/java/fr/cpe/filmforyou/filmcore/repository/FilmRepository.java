package fr.cpe.filmforyou.filmcore.repository;

import fr.cpe.filmforyou.filmcore.document.Film;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface FilmRepository extends MongoRepository<Film, String> {

    @Query(value = "{img_url: {$ne: 'https://www.teahub.io/photos/full/3-37351_sea-wallpaper-hd-portrait.jpg'}}", fields = "{ 'id': 1, 'img_url': 1, 'avg_vote' : 1, 'title': 1 }")
    @Cacheable("findAllWithNoDefaultUrl")
    Page<Film> findAllWithNoDefaultUrl(Pageable pageable);

}
