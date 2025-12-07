package com.nextepisode.user_service.repo;

import com.nextepisode.user_service.entity.movie.MovieGenre;
import com.nextepisode.user_service.entity.tv.Tv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TvRepository extends JpaRepository<Tv, Long> {
    Optional<Tv> findByName(String name);
}