package com.nextepisode.tmdb_service.service;

import com.nextepisode.tmdb_service.tmdb.response.GenreList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;

@Slf4j
@Service
public class TMDBGenreService extends BaseService {


    private final static String MOVIE_GENRE_LIST_API_PATH = "/genre/movie/list";
    private final static String TV_SHOW_GENRE_LIST_API_PATH = "/genre/tv/list";

    public TMDBGenreService(RestClient TMDBClient) {
        super(TMDBClient);
    }

    @Cacheable("movieGenres")
    public GenreList getMovieGenres() {
        log.info("Start getting movies genres");
        try {
            return fetchGenres(MOVIE_GENRE_LIST_API_PATH);
        } catch (Exception e) {
            log.error("Failed to get movie genres", e);
            throw new RuntimeException("Failed to get movie genres", e);
        }
    }

    @Cacheable("tvShowGenres")
    public GenreList getTvShowGenres() {
        log.info("Start getting tv shows genres");
        try {
            return fetchGenres(TV_SHOW_GENRE_LIST_API_PATH);
        } catch (Exception e) {
            log.error("Failed to get tv shows genres", e);
            throw new RuntimeException("Failed to get tv shows genres", e);
        }
    }


    public GenreList fetchGenres(String path) {
        log.info("Start getting genres from TMDB API");
        GenreList tmdbGenreListResponse = TMDBClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("language", "en-US")
                        .build()
                )
                .retrieve()
                .body(GenreList.class);
        return GenreList.builder()
                .genres(tmdbGenreListResponse.getGenres())
                .storedAt(Instant.now())
                .total(tmdbGenreListResponse.getGenres().size())
                .build();
    }

}
