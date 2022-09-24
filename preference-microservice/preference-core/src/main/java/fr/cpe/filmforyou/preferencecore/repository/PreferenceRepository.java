package fr.cpe.filmforyou.preferencecore.repository;

import fr.cpe.filmforyou.preferencecore.bo.Preference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    @Query("SELECT p FROM Preference p WHERE p.userId = :userId")
    List<Preference> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Preference p WHERE p.userId = :userId AND p.filmId = :filmId")
    Optional<Preference> findOneByUserIdAndFilmId(@Param("userId") Long userId, @Param("filmId") String filmId);

    @Query("SELECT p.filmId FROM Preference p GROUP BY p.filmId ORDER BY AVG(p.mark) DESC")
    Page<String> findFilmMostLoved(Pageable pageable);

    @Query("SELECT p.filmId FROM Preference p WHERE p.userId = :userId AND p.mark > 5")
    List<String> getFilmLiked(@Param("userId") Long userId);

    @Query("SELECT p.filmId FROM Preference p WHERE p.userId = :userId")
    List<String> getFilmViewed(@Param("userId") Long userId);

    @Query("DELETE FROM Preference p WHERE p.userId = :userId")
    @Modifying
    Integer deleteAllByUser(@Param("userId") Long userId);

    @Query("SELECT p FROM Preference p WHERE p.userId = :userId AND p.filmId in :filmIds")
    List<Preference> findAllByUserIdAndFilms(@Param("userId") Long userId, @Param("filmIds") List<String> filmIds);

}
