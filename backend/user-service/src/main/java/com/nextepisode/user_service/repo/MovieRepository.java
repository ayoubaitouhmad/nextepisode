package com.nextepisode.user_service.repo;


import com.nextepisode.user_service.entity.Movie;
import com.nextepisode.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findById(Long id);
    Optional<Movie> findByTitle(String title);
}