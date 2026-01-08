package com.nextepisode.user_service.repo;

import com.nextepisode.user_service.entity.tv.TVGenre;
import com.nextepisode.user_service.entity.tv.Tv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TvGenreRepository extends JpaRepository<TVGenre, Long> {
    Optional<TVGenre> findByName(String name);
}