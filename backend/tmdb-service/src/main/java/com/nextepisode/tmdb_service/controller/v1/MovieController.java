package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.dto.movie.TmdbMovieListResponse;
import com.nextepisode.tmdb_service.dto.movie.filters.MovieDiscoverFilters;
import com.nextepisode.tmdb_service.enums.movie.MovieSortBy;
import com.nextepisode.tmdb_service.service.movie.TmdbMovieClientService;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
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

        MovieDiscoverFilters filters = MovieDiscoverFilters.builder()
                .page(page)
                .language(language)
                .build();

        log.info("Filters: {}", filters.toString());
        return movieService.getTopRatedMovies(page, language);
    }

    @GetMapping("/upcoming")
    public TmdbMovieListResponse topUpcomingMovies(@RequestParam(defaultValue = "1") @Min(1) Integer page, @RequestParam(defaultValue = "en-US") String language) {
        return movieService.getUpcomingMovies(page, language);
    }

    @GetMapping("/trending")
    public TmdbMovieListResponse topUpcomingMovies(
            @RequestParam(defaultValue = "day") String timeWindow,
            @RequestParam(defaultValue = "en-US") String language
    ) {
        return movieService.getTrending(timeWindow, language);
    }


    @GetMapping("/discover")
    public TmdbMovieListResponse topUpcomingMovies(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean includeAdult,
            @RequestParam(required = false) String watchRegion,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) List<Integer> watchProviders,
            @RequestParam(required = false) List<Integer> genres

    ) {
        MovieDiscoverFilters filters = MovieDiscoverFilters.builder()
                .sortBy(MovieSortBy.fromValue(sortBy))
                .page(page)
                .language(language)
                .includeAdult(includeAdult)
                .watchRegion(watchRegion)
                .year(year)
                .yearFrom(yearFrom)
                .yearTo(yearTo)
                .watchProviders(watchProviders)
                .genres(genres)
                .build();


        return movieService.discoverMovies(filters);
    }
}
