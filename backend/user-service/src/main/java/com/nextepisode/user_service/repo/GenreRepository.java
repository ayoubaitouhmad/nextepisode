package com.nextepisode.user_service.repo;

import com.nextepisode.user_service.entity.movie.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<MovieGenre, Long> {

    Optional<MovieGenre> findByName(String name);

    @Query("SELECT g FROM MovieGenre g WHERE g.name IN :names")
    List<MovieGenre> findByNameIn(@Param("names") Set<String> names);

    @Query("SELECT g FROM MovieGenre g WHERE g.id IN :ids")
    List<MovieGenre> findByIdIn(@Param("ids") Set<Long> ids);

    @Query("SELECT DISTINCT g FROM MovieGenre g JOIN g.movies m WHERE m.id = :movieId")
    List<MovieGenre> findByMovieId(@Param("movieId") Long movieId);

    boolean existsByName(String name);

    @Query("SELECT mg FROM MovieGenre mg WHERE LOWER(mg.name) IN :names")
    List<MovieGenre> findByNamesIgnoreCase(@Param("names") List<String> names);
}