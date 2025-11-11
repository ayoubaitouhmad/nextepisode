package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.dto.TMDBMovie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RequestMapping("")
@RestController
public class Controller {


    private final RestClient tmdbRestClient;


    @Autowired
    public Controller(RestClient externalApiClient, RestClient tmdbRestClient) {
        this.tmdbRestClient = tmdbRestClient;
    }


    @GetMapping
    public TMDBMovie test(){
        return tmdbRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/1578803?language=en-US")
                        .build())
                .retrieve()
                .body(TMDBMovie.class);
    }

}
