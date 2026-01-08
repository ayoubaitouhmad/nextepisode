package com.nextepisode.user_service.controller.v1;

import com.nextepisode.user_service.config.ApiPaths;
import com.nextepisode.user_service.dto.MovieListResponse;
import com.nextepisode.user_service.dto.MovieStatus;
import com.nextepisode.user_service.dto.UserMovieTvStats;
import com.nextepisode.user_service.dto.request.MovieStatusRequest;
import com.nextepisode.user_service.service.UserMovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.API_V1 + "/movies")
@RequiredArgsConstructor
public class UserMovieController {

    private final UserMovieService userMovieService;

    @GetMapping("/favorite")
    public MovieListResponse favoriteMovies(@AuthenticationPrincipal String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("um.createdAt").descending());
        return userMovieService.getUserFavoriteMovies(username, pageable);
    }

    @GetMapping("/watched")
    public MovieListResponse watchedMovies(@AuthenticationPrincipal String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("um.watchedAt").descending());
        return userMovieService.getUserWatchedMovies(username, pageable);
    }

    @GetMapping("/watchlist")
    public MovieListResponse watchlistMovies(@AuthenticationPrincipal String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("um.createdAt").descending());
        return userMovieService.getUserWatchlistMovies(username, pageable);
    }

    @GetMapping("/stats")
    public UserMovieTvStats watchlistMovies(@AuthenticationPrincipal String username) {
        return userMovieService.getUserMoviesStats(username);
    }

    @GetMapping("/{id}/stats")
    public MovieStatus watchlistMovies(@AuthenticationPrincipal String username, @PathVariable Long id) {
        return userMovieService.getUserMoviesStats(id, username);
    }

    @PostMapping("/status")
    public ResponseEntity<MovieStatus> updateMovieStatus(
            @Valid @RequestBody MovieStatusRequest request,
            @AuthenticationPrincipal String username) {
//        log.info("Update movie status request: {}", request);
        MovieStatus movieStatus = userMovieService.changeMovieStatus(request, username);
        return ResponseEntity.ok(movieStatus);
    }

}