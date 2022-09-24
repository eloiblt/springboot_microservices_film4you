package fr.cpe.filmforyou.preferencecore.repository;

import fr.cpe.filmforyou.preferencecore.bo.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchRepository extends JpaRepository<Watch, Long> {

    @Query("SELECT w FROM Watch w WHERE w.filmId = :filmId and w.userId = :userId")
    Optional<Watch> exists(@Param("filmId") String filmId, @Param("userId") Long userId);

    @Query("DELETE FROM Watch w WHERE w.filmId = :filmId and w.userId = :userId")
    @Modifying
    Integer deleteByUserIdAndFilmId(@Param("filmId") String filmId, @Param("userId") Long userId);

    @Query("SELECT w FROM Watch w WHERE w.userId = :userId")
    List<Watch> findAllByUser(@Param("userId") Long userId);

    @Query("DELETE FROM Watch w WHERE w.userId = :userId")
    @Modifying
    Integer deleteAllByUser(@Param("userId") Long userId);

}
