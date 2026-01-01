package com.nextepisode.tmdb_service.service.movie;

import com.nextepisode.tmdb_service.service.GenreService;
import com.nextepisode.tmdb_service.tmdb.common.Genre;
import com.nextepisode.tmdb_service.tmdb.response.MovieList;
import com.nextepisode.tmdb_service.tmdb.response.MovieSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovieGenreEnricher {
    private final GenreService genreService;

    public void enrichMoviesWithGenres(MovieList movieList) {

        if (movieList == null || movieList.getResults() == null || movieList.getResults().isEmpty()) {
            log.debug("No movies to enrich with genres");
            return;
        }

        // Build genre lookup map once: O(g) where g = number of genres
        Map<Integer, Genre> genreMap = buildGenreLookupMap();

        // Enrich each movie: O(m × avg_genres_per_movie)
        long enrichedCount = movieList.getResults().stream()
                .filter(movie -> movie.getGenreIds() != null && movie.getGenreIds().length > 0)
                .peek(movie -> enrichMovieWithGenres(movie, genreMap))
                .count();

        log.debug("Enriched {} movies with genre information", enrichedCount);
    }

    /**
     * Builds a lookup map of genre ID to Genre object for efficient lookups.
     */
    private Map<Integer, Genre> buildGenreLookupMap() {
        return genreService.getMovieGenres()
                .getGenres()
                .stream()
                .collect(Collectors.toMap(
                        Genre::getId,
                        genre -> genre,
                        (existing, replacement) -> existing // Handle duplicates if any
                ));
    }

    /**
     * Enriches a single movie with genre information.
     */
    private void enrichMovieWithGenres(MovieSummary movie, Map<Integer, Genre> genreMap) {
        List<Genre> genres = Arrays.stream(movie.getGenreIds())
                .filter(genreMap::containsKey)
                .map(genreMap::get)
                .collect(Collectors.toList());

        movie.setGenres(genres);
        movie.setGenreIds(null); // Clear IDs after conversion to avoid confusion
    }

}
