package com.nextepisode.tmdb_service.service;

import com.nextepisode.tmdb_service.config.CacheConfig;
import com.nextepisode.tmdb_service.exception.ErrorCode;
import com.nextepisode.tmdb_service.exception.TmdbApiException;
import com.nextepisode.tmdb_service.service.core.BaseService;
import com.nextepisode.tmdb_service.service.movie.MovieGenreEnricher;
import com.nextepisode.tmdb_service.service.movie.MovieUriBuilder;
import com.nextepisode.tmdb_service.service.utll.ValidationUtils;
import com.nextepisode.tmdb_service.tmdb.request.MovieDiscoverFilters;
import com.nextepisode.tmdb_service.tmdb.response.MovieDetails;
import com.nextepisode.tmdb_service.tmdb.response.MovieList;
import com.nextepisode.tmdb_service.tmdb.response.MovieWatchProviderList;
import com.nextepisode.tmdb_service.tmdb.response.Watching;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Service for interacting with TMDB Movie API endpoints.
 * Handles movie retrieval, discovery, and watch provider information.
 */
@Slf4j
@Service
public class MovieService extends BaseService {

    private static final String MOVIE_BASE_PATH = "/movie";
    private static final String TRENDING_PATH = "/trending/movie";


    private final MovieGenreEnricher movieGenreEnricher;
    private final MovieUriBuilder movieUriBuilder;
    private final WatchProvidersService watchProvidersService;

    public MovieService(RestClient tmdbClient, MovieGenreEnricher movieGenreEnricher, MovieUriBuilder movieUriBuilder, WatchProvidersService watchProvidersService, RestClient.Builder builder) {
        super(tmdbClient);
        this.movieGenreEnricher = movieGenreEnricher;
        this.movieUriBuilder = movieUriBuilder;
        this.watchProvidersService = watchProvidersService;
    }

    /**
     * Retrieves detailed information for a specific movie by ID.
     *
     * @param id the TMDB movie ID
     * @return MovieDetails object containing all movie information
     * @throws TmdbApiException if movie is not found or API call fails
     */
    @Cacheable(value = CacheConfig.MOVIE_MOVIE_DETAIL, key = "#id")
    public MovieDetails getMovieById(Long id) {
        log.info("Fetching movie details for ID: {}", id);
        try {
            MovieDetails movieDetails = get(
                    builder -> builder
                            .path(MOVIE_BASE_PATH + "/" + id)
                            .build()
                    , MovieDetails.class);
            if (movieDetails == null) {
                throw new TmdbApiException(ErrorCode.MOVIE_NOT_FOUND, id.toString());
            }

            log.debug("Successfully retrieved movie details for ID: {}", id);
            return movieDetails;

        } catch (RestClientException e) {
            log.error("REST client error while fetching movie ID: {}", id, e);
            throw new TmdbApiException(ErrorCode.MOVIE_NOT_FOUND, id.toString(), e);
        }
    }

    /**
     * Retrieves a paginated list of popular movies.
     *
     * @param page     the page number (1-500)
     * @param language the language code (e.g., "en-US")
     * @return MovieList containing popular movies
     */
    @Cacheable(value = CacheConfig.MOVIE_POPULAR, key = "#page + '_' + #language")
    public MovieList getPopularMovies(Integer page, String language) {
        log.info("Fetching popular movies - page: {}, language: {}", page, language);
        return fetchMovieList(MOVIE_BASE_PATH + "/popular", page, language, "popular");
    }

    /**
     * Retrieves a paginated list of top-rated movies.
     *
     * @param page     the page number (1-500)
     * @param language the language code (e.g., "en-US")
     * @return MovieList containing top-rated movies
     */
    @Cacheable(value = CacheConfig.MOVIE_TOP_RATED, key = "#page + '_' + #language")
    public MovieList getTopRatedMovies(Integer page, String language) {
        log.info("Fetching top-rated movies - page: {}, language: {}", page, language);
        return fetchMovieList(MOVIE_BASE_PATH + "/top_rated", page, language, "top-rated");
    }

    /**
     * Retrieves a paginated list of upcoming movies.
     *
     * @param page     the page number (1-500)
     * @param language the language code (e.g., "en-US")
     * @return MovieList containing upcoming movies
     */
//    @Cacheable(value = "upcomingMovies", key = "#page + '_' + #language")
    public MovieList getUpcomingMovies(Integer page, String language) {
        log.info("Fetching upcoming movies - page: {}, language: {}", page, language);
        return fetchMovieList(MOVIE_BASE_PATH + "/upcoming", page, language, "upcoming");
    }

    /**
     * Retrieves trending movies for a specific time window.
     *
     * @param timeWindow the time window ("day" or "week")
     * @param language   the language code
     * @return MovieList containing trending movies
     */
//    @Cacheable(value = "trendingMovies", key = "#timeWindow + '_' + #language")
    public MovieList getTrendingMovies(String timeWindow, String language) {
        String validTimeWindow = ValidationUtils.validateTimeWindow(timeWindow);
        log.info("Fetching trending movies - timeWindow: {}, language: {}", validTimeWindow, language);

        String path = TRENDING_PATH + "/" + validTimeWindow;
        return fetchMovieList(path, 1, language, "trending");
    }

    /**
     * Discovers movies based on complex filtering criteria.
     *
     * @param filters the discovery filters (genres, year, providers, etc.)
     * @return MovieList containing discovered movies
     */
    public MovieList discoverMovies(MovieDiscoverFilters filters) {
        log.info("Discovering movies with filters: {}", filters);

        try {
            MovieList movieList = tmdbClient.get()
                    .uri(uriBuilder -> movieUriBuilder.buildDiscoverUri(uriBuilder, filters))
                    .retrieve()
                    .body(MovieList.class);

            if (movieList == null) {
                log.warn("Received null response from discover endpoint");
                return new MovieList(); // Return empty list instead of null
            }

            movieGenreEnricher.enrichMoviesWithGenres(movieList);
            log.debug("Successfully discovered {} movies", movieList.getResults().size());
            return movieList;

        } catch (RestClientException e) {
            log.error("Failed to discover movies with filters: {}", filters, e);
            throw new TmdbApiException(
                    ErrorCode.API_COMMUNICATION_ERROR,
                    "Failed to discover movies: " + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Retrieves watch provider information for a specific movie.
     *
     * @param movieId the TMDB movie ID
     * @param region  the ISO 3166-1 country code (e.g., "US", "GB")
     * @return Watching object containing provider information
     * @throws TmdbApiException if movie not found, no providers available, or invalid region
     */
    @Cacheable(value = CacheConfig.MOVIE_WATCH_PROVIDER, key = "#movieId + '_' + #region")
    public Watching getMovieWatchProviders(Long movieId, String region) {
        log.info("Fetching watch providers for movie ID: {}, region: {}", movieId, region);

        // Verify movie exists first
        getMovieById(movieId);

        try {
            MovieWatchProviderList providerList = tmdbClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(MOVIE_BASE_PATH + "/{id}/watch/providers")
                            .build(movieId))
                    .retrieve()
                    .body(MovieWatchProviderList.class);

            if (providerList == null || providerList.getProviders().isEmpty()) {
                throw new TmdbApiException(
                        ErrorCode.MOVIE_WATCH_PROVIDERS_NOT_FOUND,
                        movieId.toString()
                );
            }

            // Return all providers if no region specified
            if (region == null || region.isBlank()) {
                return providerList;
            }

            // Return region-specific providers
            return watchProvidersService.getRegionSpecificProviders(providerList, region, movieId);

        } catch (TmdbApiException e) {
            throw e;
        } catch (RestClientException e) {
            log.error("Failed to fetch watch providers for movie ID: {}", movieId, e);
            throw new TmdbApiException(
                    ErrorCode.API_COMMUNICATION_ERROR,
                    "Failed to fetch watch providers for movie: " + movieId,
                    e
            );
        }
    }

    // ==================== Private Helper Methods ====================

    /**
     * Generic method to fetch movie lists from various endpoints.
     */
    private MovieList fetchMovieList(String path, Integer page, String language, String listType) {
        Integer validPage = ValidationUtils.validatePage(page);
        String validLanguage = ValidationUtils.validateLanguage(language);

        try {
            MovieList movieList = tmdbClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("page", validPage)
                            .queryParam("language", validLanguage)
                            .build())
                    .retrieve()
                    .body(MovieList.class);

            if (movieList == null) {
                log.warn("Received null response from {} movies endpoint", listType);
                return new MovieList();
            }

            movieGenreEnricher.enrichMoviesWithGenres(movieList);
            log.debug("Successfully fetched {} {} movies", movieList.getResults().size(), listType);
            return movieList;

        } catch (RestClientException e) {
            log.error("Failed to fetch {} movies - page: {}, language: {}", listType, validPage, validLanguage, e);
            throw new TmdbApiException(
                    ErrorCode.API_COMMUNICATION_ERROR,
                    String.format("Failed to fetch %s movies", listType),
                    e
            );
        }
    }


}