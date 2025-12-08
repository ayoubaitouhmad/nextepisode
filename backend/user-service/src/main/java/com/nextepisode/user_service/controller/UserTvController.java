package com.nextepisode.user_service.controller;

import com.nextepisode.user_service.config.ApiPaths;
import com.nextepisode.user_service.dto.TvListResponse;
import com.nextepisode.user_service.dto.UserMovieTvStats;
import com.nextepisode.user_service.service.UserTvService;
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
@RequestMapping(ApiPaths.BASE + "/tvs")
@RequiredArgsConstructor
public class UserTvController {

    private final UserTvService userTvService;

    @GetMapping("/favorite")
    public TvListResponse favoriteShows(
            @AuthenticationPrincipal String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("um.createdAt").descending());
        return userTvService.getUserFavoriteShows(username, pageable);
    }

    @GetMapping("/watched")
    public TvListResponse watchedShows(
            @AuthenticationPrincipal String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("um.createdAt").descending());
        return userTvService.getUserWatchedShows(username, pageable);
    }

    @GetMapping("/watchlist")
    public TvListResponse watchListShows(
            @AuthenticationPrincipal String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("um.createdAt").descending());
        return userTvService.getUserWatchlistShows(username, pageable);
    }


    @GetMapping("/stats")
    public UserMovieTvStats watchlistMovies(
            @AuthenticationPrincipal String username
    ) {
        return userTvService.getUserMoviesStats(username);
    }


}