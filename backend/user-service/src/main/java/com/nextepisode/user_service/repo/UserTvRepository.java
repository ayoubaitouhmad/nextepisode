package com.nextepisode.user_service.repo;

import com.nextepisode.user_service.dto.MovieResponse;
import com.nextepisode.user_service.dto.TvResponse;
import com.nextepisode.user_service.dto.UserMovieTvStats;
import com.nextepisode.user_service.entity.user.UserTv;
import com.nextepisode.user_service.entity.user.UserTvId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTvRepository extends JpaRepository<UserTv, UserTvId> {


    @Query("SELECT NEW com.nextepisode.user_service.dto.TvResponse(t , um.createdAt) " +
            "FROM Tv t JOIN UserTv um ON um.tv.id = t.id " +
            "WHERE um.user.username = :username AND um.isFavorite = true")
    Page<TvResponse> findUserFavoriteMovies(@Param("username") String username, Pageable pageable);

    @Query("SELECT NEW com.nextepisode.user_service.dto.TvResponse(t , ut.createdAt , ut.watchedAt) " +
            "FROM Tv t JOIN UserTv ut ON ut.tv.id = t.id " +
            "WHERE ut.user.username = :username AND ut.watched = true")
    Page<TvResponse> findUserWatchedMovies(@Param("username") String username, Pageable pageable);

    @Query("SELECT NEW com.nextepisode.user_service.dto.TvResponse(t , ut.createdAt) " +
            "FROM Tv t JOIN UserTv ut ON ut.tv.id = t.id " +
            "WHERE ut.user.username = :username AND ut.inWatchlist = true")
    Page<TvResponse> findUserWatchlistMovies(@Param("username") String username, Pageable pageable);

    @Query("SELECT " +
            "SUM(CASE WHEN ut.isFavorite = true THEN 1 ELSE 0 END) AS favoriteCount, " +
            "SUM(CASE WHEN ut.inWatchlist = true THEN 1 ELSE 0 END) AS watchlistCount, " +
            "SUM(CASE WHEN ut.watched = true THEN 1 ELSE 0 END) AS watchedCount " +
            "FROM UserTv ut WHERE ut.user.username = :username")
    UserMovieTvStats getUserMovieStats(@Param("username") String username);

}