package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.enums.movie.MovieSortBy;
import com.nextepisode.tmdb_service.service.MovieService;
import com.nextepisode.tmdb_service.tmdb.request.MovieDiscoverFilters;
import com.nextepisode.tmdb_service.tmdb.response.MovieDetails;
import com.nextepisode.tmdb_service.tmdb.response.MovieList;
import com.nextepisode.tmdb_service.tmdb.response.Watching;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for movie-related endpoints.
 * Provides access to TMDB movie data including details, lists, and watch providers.
 *
 * <p>All endpoints are prefixed with {@code /api/v1/movies}</p>
 *
 * @author NextEpisode Team
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RequestMapping(ApiPaths.API_V1 + "/movies")
@RestController
public class MovieController {

    private final MovieService movieService;

    /**
     * Constructs a new MovieController with the required dependencies.
     *
     * @param movieService the service for movie operations
     */
    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Retrieves detailed information for a specific movie.
     *
     * <p><b>Endpoint:</b> {@code GET /api/v1/movies/{id}}</p>
     * <p><b>Example:</b> {@code GET /api/v1/movies/550} (Fight Club)</p>
     *
     * @param id the TMDB movie ID (required, must be positive)
     * @return MovieDetails object containing comprehensive movie information including
     *         title, overview, release date, ratings, runtime, budget, revenue, etc.
     * @throws com.nextepisode.tmdb_service.exception.TmdbApiException if movie not found
     */
    @GetMapping("/{id}")
    public MovieDetails getMovieById(
            @PathVariable(required = true) Long id
    ) {
        log.debug("REST request to get movie by ID: {}", id);
        return movieService.getMovieById(id);
    }

    /**
     * Retrieves a paginated list of currently popular movies.
     *
     * <p><b>Endpoint:</b> {@code GET /api/v1/movies/popular}</p>
     * <p><b>Example:</b> {@code GET /api/v1/movies/popular?page=1&language=en-US}</p>
     *
     * <p>Popularity is determined by TMDB's proprietary algorithm based on
     * views, votes, and other engagement metrics.</p>
     *
     * @param page the page number (optional, min: 1, default: 1, max: 500)
     * @param language the ISO 639-1 language code (optional, default: "en-US")
     *                 Examples: "en-US", "fr-FR", "es-ES"
     * @return MovieList containing popular movies with pagination info
     */
    @GetMapping("/popular")
    public MovieList popularMovies(
            @RequestParam(required = false) @Min(1) Integer page,
            @RequestParam(defaultValue = "en-US") String language
    ) {
        log.debug("REST request to get popular movies - page: {}, language: {}", page, language);
        return movieService.getPopularMovies(page, language);
    }

    /**
     * Retrieves a paginated list of top-rated movies of all time.
     *
     * <p><b>Endpoint:</b> {@code GET /api/v1/movies/top-rated}</p>
     * <p><b>Example:</b> {@code GET /api/v1/movies/top-rated?page=1&language=en-US}</p>
     *
     * <p>Top-rated movies are determined by user ratings (vote average) on TMDB.
     * Movies typically need a minimum number of votes to appear in this list.</p>
     *
     * @param page the page number (default: 1, min: 1, max: 500)
     * @param language the ISO 639-1 language code (default: "en-US")
     * @return MovieList containing top-rated movies with pagination info
     */
    @GetMapping("/top-rated")
    public MovieList topRatedMovies(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "en-US") String language
    ) {
        log.debug("REST request to get top-rated movies - page: {}, language: {}", page, language);
        return movieService.getTopRatedMovies(page, language);
    }

    /**
     * Retrieves a paginated list of upcoming movies.
     *
     * <p><b>Endpoint:</b> {@code GET /api/v1/movies/upcoming}</p>
     * <p><b>Example:</b> {@code GET /api/v1/movies/upcoming?page=1&language=en-US}</p>
     *
     * <p>Upcoming movies are those with release dates in the future.
     * The list is ordered by release date (earliest first).</p>
     *
     * @param page the page number (default: 1, min: 1, max: 500)
     * @param language the ISO 639-1 language code (default: "en-US")
     * @return MovieList containing upcoming movies with pagination info
     */
    @GetMapping("/upcoming")
    public MovieList upcomingMovies(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "en-US") String language
    ) {
        log.debug("REST request to get upcoming movies - page: {}, language: {}", page, language);
        return movieService.getUpcomingMovies(page, language);
    }

    /**
     * Retrieves trending movies for a specified time window.
     *
     * <p><b>Endpoint:</b> {@code GET /api/v1/movies/trending}</p>
     * <p><b>Example:</b> {@code GET /api/v1/movies/trending?timeWindow=week&language=en-US}</p>
     *
     * <p>Trending is calculated based on recent activity including views,
     * ratings, and other engagement metrics within the specified time window.</p>
     *
     * @param timeWindow the time window for trending calculation (default: "day")
     *                   Valid values: "day" or "week"
     * @param language the ISO 639-1 language code (default: "en-US")
     * @return MovieList containing trending movies for the specified time window
     */
    @GetMapping("/trending")
    public MovieList trendingMovies(
            @RequestParam(defaultValue = "day") String timeWindow,
            @RequestParam(defaultValue = "en-US") String language
    ) {
        log.debug("REST request to get trending movies - timeWindow: {}, language: {}", timeWindow, language);
        return movieService.getTrendingMovies(timeWindow, language);
    }

    /**
     * Retrieves watch provider information for a specific movie.
     *
     * <p><b>Endpoint:</b> {@code GET /api/v1/movies/{id}/watch-providers}</p>
     * <p><b>Example:</b> {@code GET /api/v1/movies/550/watch-providers?region=US}</p>
     *
     * <p>Watch providers include streaming services, rental platforms, and purchase options.
     * Results vary by region based on content licensing agreements.</p>
     *
     * <p>If no region is specified, returns watch providers for all available regions.
     * If a region is specified but has no providers, an exception is thrown.</p>
     *
     * @param id the TMDB movie ID (required)
     * @param region the ISO 3166-1 country code (optional)
     *               Examples: "US", "GB", "FR", "CA", "AU"
     *               If null, returns providers for all regions
     * @return Watching object containing provider information
     *         - If region specified: CountryWatchProviderList for that region
     *         - If region not specified: MovieWatchProviderList for all regions
     * @throws com.nextepisode.tmdb_service.exception.TmdbApiException
     *         if movie not found or no providers available for specified region
     */
    @GetMapping("/{id}/watch-providers")
    public Watching movieWatchProviderList(
            @PathVariable() Long id,
            @RequestParam(required = false) String region
    ) {
        log.debug("REST request to get watch providers for movie ID: {}, region: {}", id, region);
        return movieService.getMovieWatchProviders(id, region);
    }

    /**
     * Discovers movies based on multiple filtering criteria.
     *
     * <p><b>Endpoint:</b> {@code GET /api/v1/movies/discover}</p>
     * <p><b>Example:</b> {@code GET /api/v1/movies/discover?sortBy=popularity.desc&year=2023&genres=28,12}</p>
     *
     * <p>This is the most powerful endpoint for finding movies. It allows combining
     * multiple filters including genres, year ranges, watch providers, ratings, and more.</p>
     *
     * <p><b>Common Use Cases:</b></p>
     * <ul>
     *   <li>Find action movies from 2023: {@code ?year=2023&genres=28}</li>
     *   <li>Find highly-rated dramas on Netflix: {@code ?genres=18&watchProviders=8&sortBy=vote_average.desc}</li>
     *   <li>Find movies from 2020-2023: {@code ?yearFrom=2020&yearTo=2023}</li>
     * </ul>
     *
     * @param sortBy the sort order (optional, default: "popularity.desc")
     *               Examples: "popularity.desc", "vote_average.desc", "release_date.desc"
     * @param page the page number (optional, min: 1, default: 1, max: 500)
     * @param language the ISO 639-1 language code (optional, default: "en-US")
     * @param includeAdult whether to include adult content (optional, default: false)
     * @param includeVideos whether to include movies with videos (optional)
     * @param region the ISO 3166-1 country code for filtering by watch providers (optional)
     *               Required when using watchProviders parameter
     * @param year filter by specific release year (optional)
     *             Note: If set, yearFrom and yearTo are ignored
     * @param yearFrom filter by minimum release year (optional, inclusive)
     *                 Used for year range filtering when year is not set
     * @param yearTo filter by maximum release year (optional, inclusive)
     *               Used for year range filtering when year is not set
     * @param watchProviders list of watch provider IDs to filter by (optional)
     *                       Examples: [8] for Netflix, [9] for Amazon Prime, [337] for Disney+
     *                       Requires region parameter to be set
     * @param genres list of genre IDs to filter by (optional)
     *               Examples: [28] for Action, [35] for Comedy, [18] for Drama
     *               Multiple genres are combined with AND logic
     * @param certification content rating filter (optional)
     *                      Examples: "G", "PG", "PG-13", "R", "NC-17" (US)
     *                      Note: Certification systems vary by country
     * @return MovieList containing movies matching the specified filters with pagination info
     *
     * @see MovieSortBy for available sort options
     * @see MovieDiscoverFilters for complete filter documentation
     */
    @GetMapping("/discover")
    public MovieList discoverMovies(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Boolean includeAdult,
            @RequestParam(required = false) Boolean includeVideos,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) List<Integer> watchProviders,
            @RequestParam(required = false) List<Integer> genres,
            @RequestParam(required = false) String certification
    ) {
        log.debug("REST request to discover movies with filters - sortBy: {}, page: {}, year: {}, genres: {}",
                sortBy, page, year, genres);

        // Build filters object from request parameters
        MovieDiscoverFilters filters = MovieDiscoverFilters.builder()
                .sortBy(MovieSortBy.fromValue(sortBy))
                .page(page)
                .language(language)
                .includeAdult(includeAdult)
                .region(region)
                .year(year)
                .yearFrom(yearFrom)
                .yearTo(yearTo)
                .watchProviders(watchProviders)
                .genres(genres)
                .includeVideos(includeVideos)
                .certification(certification)
                .build();

        return movieService.discoverMovies(filters);
    }
}