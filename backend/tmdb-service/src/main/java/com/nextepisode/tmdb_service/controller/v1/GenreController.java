package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.tmdb.response.TMDBGenreListResponse;
import com.nextepisode.tmdb_service.service.TMDBGenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(ApiPaths.API_V1 + "/genres")
@RestController
public class GenreController {

    private final TMDBGenreService tmdbGenreService;

    public GenreController(TMDBGenreService tmdbGenreService) {
        this.tmdbGenreService = tmdbGenreService;
    }

    @GetMapping("/movie-genres")
    public TMDBGenreListResponse moviesGenres() {
        return tmdbGenreService.getMovieGenres();
    }
    @GetMapping("/tv-show-genres")
    public TMDBGenreListResponse tvShowGenres() {
        return tmdbGenreService.getTvShowGenres();
    }


}
