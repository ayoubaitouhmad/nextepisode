package com.nextepisode.tmdb_service.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {


    @Value("${tmdb.api.key}")
    private String TMDBApiKey;

    @Value("${tmdb.api.token}")
    private String TMDBApiToken;

    @Value("${tmdb.api.base-url}")
    private String TMDBBaseUrl;



    private String authorizationToken() {;
        return "Bearer " + TMDBApiToken;
    }







    @Bean
    public RestClient TMDBRestClient() {
        return RestClient.builder()
                .baseUrl(this.TMDBBaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, authorizationToken())
                .build();
    }


}


