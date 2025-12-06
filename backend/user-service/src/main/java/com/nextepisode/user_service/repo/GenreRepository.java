package com.nextepisode.user_service.repo;

import com.nextepisode.user_service.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

    @Query("SELECT g FROM Genre g WHERE g.name IN :names")
    List<Genre> findByNameIn(@Param("names") Set<String> names);

    @Query("SELECT g FROM Genre g WHERE g.id IN :ids")
    List<Genre> findByIdIn(@Param("ids") Set<Long> ids);

    @Query("SELECT DISTINCT g FROM Genre g JOIN g.movies m WHERE m.id = :movieId")
    List<Genre> findByMovieId(@Param("movieId") Long movieId);

    boolean existsByName(String name);
}