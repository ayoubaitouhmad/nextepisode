package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.dto.movie.TmdbMovieListResponse;
import com.nextepisode.tmdb_service.service.TmdbMovieClientService;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/movies")
@RestController
public class MovieController {

    private final TmdbMovieClientService movieService;


    @Autowired
    public MovieController(TmdbMovieClientService restTMDBMovieClient) {
        this.movieService = restTMDBMovieClient;

    }


    @GetMapping("/popular")
    public TmdbMovieListResponse popularMovies(@RequestParam(required = false) @Min(1) Integer page, @RequestParam(defaultValue = "en-US") String language) {
        return movieService.getPopularMovies(page, language);
    }

    @GetMapping("/top-rated")
    public TmdbMovieListResponse topRatedMovies(@RequestParam(defaultValue = "1") @Min(1) Integer page, @RequestParam(defaultValue = "en-US") String language) {
        return movieService.getTopRatedMovies(page, language);
    }

    @GetMapping("/upcoming")
    public TmdbMovieListResponse topUpcomingMovies(@RequestParam(defaultValue = "1") @Min(1) Integer page, @RequestParam(defaultValue = "en-US") String language) {
        return movieService.getUpcomingMovies(page, language);
    }

}
