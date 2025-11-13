package com.nextepisode.tmdb_service.service.movie;

import com.nextepisode.tmdb_service.dto.movie.TmdbMovieListResponse;
import com.nextepisode.tmdb_service.dto.movie.filters.MovieDiscoverFilters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TmdbMovieClientService {

    private final RestClient TMDBMovieClient;


    @Autowired
    public TmdbMovieClientService(RestClient TMDBMovieClient) {
        this.TMDBMovieClient = TMDBMovieClient;
    }

    public TmdbMovieListResponse getPopularMovies(Integer page, String language) {
        log.info("Fetching popular movies - language: {}, page: {}", language, page);
        return fetchMovies("/movie/popular", page, language);
    }

    public TmdbMovieListResponse getTopRatedMovies(Integer page, String language) {
        log.info("Fetching top rated movies - language: {}, page: {}", language, page);
        return fetchMovies("/movie/top_rated", page, language);
    }

    public TmdbMovieListResponse getUpcomingMovies(Integer page, String language) {
        log.info("Fetching popular movies - language: {}, page: {}", language, page);
        return fetchMovies("/movie/upcoming", page, language);
    }


    public TmdbMovieListResponse fetchMovies(String path, Integer page, String language) {
        try {
            return TMDBMovieClient.get().uri(
                            uriBuilder -> uriBuilder
                                    .path(path)
                                    .queryParam("page", validatePage(page))
                                    .queryParam("language", validateLanguage(language)).build())
                    .retrieve()
                    .body(TmdbMovieListResponse.class);

        } catch (Exception e) {
            // Log error and handle appropriately
            throw new RuntimeException("Failed to fetch movies", e);
        }
    }


    private Integer validatePage(Integer page) {
        return (page == null || page < 1) ? 1 : page;
    }

    private String validateLanguage(String language) {
        return (language == null || language.isBlank()) ? "en-US" : language;
    }


    public TmdbMovieListResponse getTrending(String timeWindow, String language) {
        try {
            return TMDBMovieClient.get().uri(uriBuilder -> uriBuilder.path("/trending/movie/week").queryParam("time_window", timeWindow).queryParam("language", validateLanguage(language)).build()).retrieve().body(TmdbMovieListResponse.class);

        } catch (Exception e) {
            // Log error and handle appropriately
            throw new RuntimeException("Failed to fetch movies", e);
        }
    }


    public TmdbMovieListResponse discoverMovies(MovieDiscoverFilters filters) {
        log.info("Discovering movies filters - {}", filters.toString());

        try {
            return TMDBMovieClient.get()
                    .uri(uriBuilder -> buildDiscoverUri(uriBuilder, filters))
                    .retrieve()
                    .body(TmdbMovieListResponse.class);
        } catch (Exception e) {
            log.error("Failed to discover movies", e);
            throw new RuntimeException("Failed to discover movies", e);
        }
    }

    private URI buildDiscoverUri(UriBuilder uriBuilder, MovieDiscoverFilters filters) {
        uriBuilder.path("/discover/movie")
                .queryParam("sort_by", filters.getSortBy())
                .queryParam("page", validatePage(filters.getPage()))
                .queryParam("language", filters.getLanguage())
                .queryParam("include_adult", filters.getIncludeAdult())
                .queryParam("watch_region", filters.getWatchRegion())
                .queryParam("vote_count.gte", "50");

        // Add year filters
        if (filters.getYear() != null) {
            uriBuilder.queryParam("primary_release_year", filters.getYear());
        } else {
            if (filters.getYearFrom() != null) {
                uriBuilder.queryParam("primary_release_date.gte",
                        filters.getYearFrom() + "-01-01");
            }
            if (filters.getYearTo() != null) {
                uriBuilder.queryParam("primary_release_date.lte",
                        filters.getYearTo() + "-12-31");
            }
        }

        // Add watch providers
        if (filters.getWatchProviders() != null && !filters.getWatchProviders().isEmpty()) {
            uriBuilder.queryParam("with_watch_providers", filters.getWatchProvidersIdsAsString());
        }

        // Add genres
        if (filters.getGenres() != null && !filters.getGenres().isEmpty()) {
            uriBuilder.queryParam("with_genres", filters.getGenreIdsAsString());
        }

        log.info("Discovering movies URI - {}", uriBuilder.toUriString());

        return uriBuilder.build();
    }




}
