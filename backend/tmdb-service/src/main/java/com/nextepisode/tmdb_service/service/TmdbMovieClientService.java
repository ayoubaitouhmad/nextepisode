package com.nextepisode.tmdb_service.service;

import com.nextepisode.tmdb_service.dto.movie.TmdbMovieListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class TmdbMovieClientService {

    private final RestClient TMDBMovieClient;

    @Autowired
    public TmdbMovieClientService(RestClient restTMDBMovieClient) {
        this.TMDBMovieClient = restTMDBMovieClient;
    }

    public TmdbMovieListResponse getPopularMovies(Integer page, String language) {
        log.info("Fetching popular movies - language: {}, page: {}", language, page);
        return fetchMovies("/popular", page, language);
    }

    public TmdbMovieListResponse getTopRatedMovies(Integer page, String language) {
        log.info("Fetching top rated movies - language: {}, page: {}", language, page);
        return fetchMovies("/top_rated", page, language);
    }

    public TmdbMovieListResponse getUpcomingMovies(Integer page, String language) {
        log.info("Fetching popular movies - language: {}, page: {}", language, page);
        return fetchMovies("/upcoming", page, language);
    }

    public TmdbMovieListResponse fetchMovies(String path, Integer page, String language) {
        try {
            return TMDBMovieClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("page", validatePage(page))
                            .queryParam("language", validateLanguage(language))
                            .build()
                    )
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

}
