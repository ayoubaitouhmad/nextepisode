package com.nextepisode.user_service.repo;

import com.nextepisode.user_service.dto.MovieResponse;
import com.nextepisode.user_service.entity.UserMovie;
import com.nextepisode.user_service.entity.UserMovieId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserMovieRepository extends JpaRepository<UserMovie, UserMovieId> {

    @Query("SELECT NEW com.nextepisode.user_service.dto.MovieResponse(m.id, m.title, um.createdAt) " +
            "FROM Movie m JOIN UserMovie um ON um.movie.id = m.id " +
            "WHERE um.user.username = :username AND um.isFavorite = true")
    Page<MovieResponse> findUserFavoriteMovies(@Param("username") String username, Pageable pageable);

    @Query("SELECT NEW com.nextepisode.user_service.dto.MovieResponse(m.id, m.title, um.createdAt, um.watchedAt) " +
            "FROM Movie m JOIN UserMovie um ON um.movie.id = m.id " +
            "WHERE um.user.username = :username AND um.watched = true")
    Page<MovieResponse> findUserWatchedMovies(@Param("username") String username, Pageable pageable);

    @Query("SELECT NEW com.nextepisode.user_service.dto.MovieResponse(m.id, m.title, um.createdAt) " +
            "FROM Movie m JOIN UserMovie um ON um.movie.id = m.id " +
            "WHERE um.user.username = :username AND um.inWatchlist = true")
    Page<MovieResponse> findUserWatchlistMovies(@Param("username") String username, Pageable pageable);

}