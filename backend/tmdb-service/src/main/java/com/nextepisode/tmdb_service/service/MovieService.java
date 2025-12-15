package com.nextepisode.tmdb_service.service;

import com.nextepisode.tmdb_service.tmdb.common.Genre;
import com.nextepisode.tmdb_service.tmdb.request.MovieDiscoverFilters;
import com.nextepisode.tmdb_service.tmdb.response.GenreList;
import com.nextepisode.tmdb_service.tmdb.response.MovieDetails;
import com.nextepisode.tmdb_service.tmdb.response.MovieList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MovieService extends BaseService {

    private static GenreService genreService = null;


    public MovieService(RestClient tmdbClient, GenreService genreService) {
        super(tmdbClient);
        MovieService.genreService = genreService;
    }

    public MovieDetails getMovieById(Long id) {
        log.info("Start getting movie with id {} ", id);
        try {
            return tmdbClient.get().uri(uriBuilder -> uriBuilder.path("/movie/" + id).build()).retrieve().body(MovieDetails.class);
        } catch (Exception e) {
            log.error("Error getting movie with id {} ", id, e);
            throw new RuntimeException("Try again later");
        }
    }

    public MovieList getPopularMovies(Integer page, String language) {
        log.info("Fetching popular movies - language: {}, page: {}", language, page);
        return fetchMovies("/movie/popular", page, language);
    }

    public MovieList getTopRatedMovies(Integer page, String language) {
        log.info("Fetching top rated movies - language: {}, page: {}", language, page);
        return fetchMovies("/movie/top_rated", page, language);
    }

    public MovieList getUpcomingMovies(Integer page, String language) {
        log.info("Fetching popular movies - language: {}, page: {}", language, page);
        return fetchMovies("/movie/upcoming", page, language);
    }


    public MovieList fetchMovies(String path, Integer page, String language) {
        try {
            MovieList movieList = tmdbClient.get().uri(uriBuilder -> uriBuilder.path(path).queryParam("page", validatePage(page)).queryParam("language", validateLanguage(language)).build()).retrieve().body(MovieList.class);
            enrichMoviesWithGenres(movieList);
            return movieList;
        } catch (Exception e) {
            // Log error and handle appropriately
            throw new RuntimeException("Failed to fetch movies", e);
        }
    }

    public static void enrichMoviesWithGenres(MovieList movieList) {
        if (movieList == null || movieList.getResults() == null) {
            return;
        }
        // Build lookup map once: O(g) where g = number of genres
        Map<Integer, Genre> genreMap = genreService.getMovieGenres()
                .getGenres()
                .stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre));

        // Enrich each movie: O(m × avg_genres_per_movie)
        movieList.getResults().forEach(movie -> {
            if (movie.getGenreIds() == null) {
                return;
            }

            List<Genre> genres = Arrays.stream(movie.getGenreIds())
                    .filter(genreMap::containsKey)
                    .map(genreMap::get)
                    .collect(Collectors.toList());

            movie.setGenres(genres);
            movie.setGenreIds(null);
        });
    }


    private Integer validatePage(Integer page) {
        return (page == null || page < 1) ? 1 : page;
    }

    private String validateLanguage(String language) {
        return (language == null || language.isBlank()) ? "en-US" : language;
    }


    public MovieList getTrending(String timeWindow, String language) {
        try {
            MovieList movieList = tmdbClient.get().uri(uriBuilder -> uriBuilder
                            .path("/trending/movie/week")
                            .queryParam("time_window", timeWindow)
                            .queryParam("language", validateLanguage(language))
                            .build())
                    .retrieve()
                    .body(MovieList.class);
            enrichMoviesWithGenres(movieList);
            return movieList;

        } catch (Exception e) {
            // Log error and handle appropriately
            throw new RuntimeException("Failed to fetch movies", e);
        }
    }


    public MovieList discoverMovies(MovieDiscoverFilters filters) {
        log.info("Discovering movies filters - {}", filters.toString());

        try {
            return tmdbClient.get().uri(uriBuilder -> buildDiscoverUri(uriBuilder, filters)).retrieve().body(MovieList.class);
        } catch (Exception e) {
            log.error("Failed to discover movies", e);
            throw new RuntimeException("Failed to discover movies", e);
        }
    }

    private URI buildDiscoverUri(UriBuilder uriBuilder, MovieDiscoverFilters filters) {
        uriBuilder.path("/discover/movie").queryParam("sort_by", filters.getSortBy()).queryParam("page", validatePage(filters.getPage())).queryParam("language", filters.getLanguage()).queryParam("include_adult", filters.getIncludeAdult()).queryParam("watch_region", filters.getWatchRegion()).queryParam("vote_count.gte", "50");

        // Add year filters
        if (filters.getYear() != null) {
            uriBuilder.queryParam("primary_release_year", filters.getYear());
        } else {
            if (filters.getYearFrom() != null) {
                uriBuilder.queryParam("primary_release_date.gte", filters.getYearFrom() + "-01-01");
            }
            if (filters.getYearTo() != null) {
                uriBuilder.queryParam("primary_release_date.lte", filters.getYearTo() + "-12-31");
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
