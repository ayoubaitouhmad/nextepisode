package com.nextepisode.user_service.controller;

import com.nextepisode.user_service.config.ApiPaths;
import com.nextepisode.user_service.dto.MovieListResponse;
import com.nextepisode.user_service.service.UserMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.BASE + "/movies")
@RequiredArgsConstructor
public class UserMovieController {

    private final UserMovieService userMovieService;

    @GetMapping("/favorite")
    public MovieListResponse favoriteMovies(
            @AuthenticationPrincipal String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("um.createdAt").descending());
        return userMovieService.getUserFavoriteMovies(username, pageable);
    }

    @GetMapping("/watched")
    public MovieListResponse watchedMovies(
            @AuthenticationPrincipal String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("um.watchedAt").descending());
        return userMovieService.getUserWatchedMovies(username, pageable);
    }

    @GetMapping("/watchlist")
    public MovieListResponse watchlistMovies(
            @AuthenticationPrincipal String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("um.createdAt").descending());
        return userMovieService.getUserWatchlistMovies(username, pageable);
    }

}