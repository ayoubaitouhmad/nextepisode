package com.nextepisode.user_service.repo;


import com.nextepisode.user_service.entity.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findById(Long id);
    Optional<Movie> findByTitle(String title);
}