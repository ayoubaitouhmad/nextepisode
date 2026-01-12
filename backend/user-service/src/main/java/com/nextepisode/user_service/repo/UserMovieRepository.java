package com.nextepisode.user_service.repo;

import com.nextepisode.user_service.dto.MovieResponse;
import com.nextepisode.user_service.dto.MovieStatus;
import com.nextepisode.user_service.dto.UserMovieTvStats;
import com.nextepisode.user_service.entity.user.UserMovie;
import com.nextepisode.user_service.entity.user.UserMovieId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, UserMovieId> {

    @Query("SELECT NEW com.nextepisode.user_service.dto.MovieResponse(m , um.createdAt) " +
            "FROM Movie m JOIN UserMovie um ON um.movie.id = m.id " +
            "WHERE um.user.username = :username AND um.isFavorite = true")
    Page<MovieResponse> findUserFavoriteMovies(@Param("username") String username, Pageable pageable);

    @Query("SELECT NEW com.nextepisode.user_service.dto.MovieResponse(m , um.createdAt , um.watchedAt) " +
            "FROM Movie m JOIN UserMovie um ON um.movie.id = m.id " +
            "WHERE um.user.username = :username AND um.watched = true")
    Page<MovieResponse> findUserWatchedMovies(@Param("username") String username, Pageable pageable);

    @Query("SELECT NEW com.nextepisode.user_service.dto.MovieResponse(m , um.createdAt) " +
            "FROM Movie m JOIN UserMovie um ON um.movie.id = m.id " +
            "WHERE um.user.username = :username AND um.inWatchlist = true")
    Page<MovieResponse> findUserWatchlistMovies(@Param("username") String username, Pageable pageable);

    @Query("SELECT " +
            "SUM(CASE WHEN um.isFavorite = true THEN 1 ELSE 0 END) AS favoriteCount, " +
            "SUM(CASE WHEN um.inWatchlist = true THEN 1 ELSE 0 END) AS watchlistCount, " +
            "SUM(CASE WHEN um.watched = true THEN 1 ELSE 0 END) AS watchedCount " +
            "FROM UserMovie um WHERE um.user.username = :username")
    UserMovieTvStats getUserMovieStats(@Param("username") String username);

    @Query("SELECT new com.nextepisode.user_service.dto.MovieStatus(um.isFavorite, um.watched, um.inWatchlist) " +
            "FROM UserMovie um WHERE um.movie.id = :movieId AND um.user.username = :username")
    Optional<MovieStatus> findMovieStatus(@Param("movieId") Long movieId,
                                          @Param("username") String username);

    Optional<UserMovie> findByMovieIdAndUserUsername(Long movieId, String userUsername);

    boolean existsByUserUsernameAndMovieId(String userUsername, Long movieId);

    @Query("SELECT um from UserMovie um  WHERE um.user.username = :username AND um.movie.id IN :movieIds")
    List<UserMovie> findByUserUsernameAndMovieIdIn(
            String username,
            List<Integer> movieIds
    );

}