package fr.cpe.filmforyou.usercore.repository;

import fr.cpe.filmforyou.usercore.bo.UserSearchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSearchViewRepository extends JpaRepository<UserSearchView, Long> {
}
