package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.enums.movie.MovieSortBy;
import com.nextepisode.tmdb_service.service.MovieService;
import com.nextepisode.tmdb_service.tmdb.request.MovieDiscoverFilters;
import com.nextepisode.tmdb_service.tmdb.response.MovieDetails;
import com.nextepisode.tmdb_service.tmdb.response.MovieList;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping(ApiPaths.API_V1 + "/movies")
@RestController
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{id}")
    public MovieDetails popularMovies(
            @PathVariable(required = true) Long id
    ) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/popular")
    public MovieList popularMovies(@RequestParam(required = false) @Min(1) Integer page, @RequestParam(defaultValue = "en-US") String language) {
        return movieService.getPopularMovies(page, language);
    }

    @GetMapping("/top-rated")
    public MovieList topRatedMovies(@RequestParam(defaultValue = "1") @Min(1) Integer page, @RequestParam(defaultValue = "en-US") String language) {
        MovieDiscoverFilters filters = MovieDiscoverFilters.builder()
                .page(page)
                .language(language)
                .build();
        return movieService.getTopRatedMovies(page, language);
    }

    @GetMapping("/upcoming")
    public MovieList upcomingMovies(@RequestParam(defaultValue = "1") @Min(1) Integer page, @RequestParam(defaultValue = "en-US") String language) {
        return movieService.getUpcomingMovies(page, language);
    }

    @GetMapping("/trending")
    public MovieList trendingMovies(
            @RequestParam(defaultValue = "day") String timeWindow,
            @RequestParam(defaultValue = "en-US") String language
    ) {
        return movieService.getTrending(timeWindow, language);
    }


    @GetMapping("/discover")
    public MovieList discoverMovies(
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
