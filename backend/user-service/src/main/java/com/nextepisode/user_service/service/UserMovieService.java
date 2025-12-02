package com.nextepisode.user_service.service;

import com.nextepisode.user_service.dto.MovieListResponse;
import com.nextepisode.user_service.dto.MovieResponse;
import com.nextepisode.user_service.dto.UserMovieStats;
import com.nextepisode.user_service.repo.UserMovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMovieService {

    private final UserMovieRepository repo;

    @Transactional(readOnly = true)
    public MovieListResponse getUserFavoriteMovies(String username, Pageable pageable) {
        log.debug("Finding user:{} favorite movies, page:{}", username, pageable.getPageNumber());
        Page<MovieResponse> page = repo.findUserFavoriteMovies(username, pageable);
        return buildMovieListResponse(page);
    }

    @Transactional(readOnly = true)
    public MovieListResponse getUserWatchedMovies(String username, Pageable pageable) {
        log.debug("Finding user:{} watched movies, page:{}", username, pageable.getPageNumber());
        Page<MovieResponse> page = repo.findUserWatchedMovies(username, pageable);
        return buildMovieListResponse(page);
    }

    @Transactional(readOnly = true)
    public MovieListResponse getUserWatchlistMovies(String username, Pageable pageable) {
        log.debug("Finding user:{} watchlist movies, page:{}", username, pageable.getPageNumber());
        Page<MovieResponse> page = repo.findUserWatchlistMovies(username, pageable);
        return buildMovieListResponse(page);
    }


    @Transactional(readOnly = true)
    public UserMovieStats getUserMoviesStats(String username){
        return  repo.getUserMovieStats(username);
    }

    private MovieListResponse buildMovieListResponse(Page<MovieResponse> page) {
        return MovieListResponse.builder()
                .page(page.getNumber())
                .results(page.getContent())
                .totalPages((long) page.getTotalPages())
                .totalResults(page.getTotalElements())
                .build();
    }

}