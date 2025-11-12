package com.nextepisode.tmdb_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restTMDBMovieClient() {
        return RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3/movie")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDgxNTY1ZmE5MGMwMzBkYWNhZmQ4YmM3MjQwODg0ZiIsIm5iZiI6MTY3NjY2NDQ4Ny4yOTYsInN1YiI6IjYzZWZkZWE3Y2FhY2EyMDBhMTlhNjU1NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.5Lbprj-n6HfFkNN6euM9luzi9DUYuafyPJTP3Wy-xgw")
                .build();
    }

    @Bean
    public RestClient restTMDBTvShowClient() {
        return RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3/tv")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDgxNTY1ZmE5MGMwMzBkYWNhZmQ4YmM3MjQwODg0ZiIsIm5iZiI6MTY3NjY2NDQ4Ny4yOTYsInN1YiI6IjYzZWZkZWE3Y2FhY2EyMDBhMTlhNjU1NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.5Lbprj-n6HfFkNN6euM9luzi9DUYuafyPJTP3Wy-xgw")
                .build();
    }
}


