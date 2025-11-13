package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.dto.movie.response.TMDBCountryListResponse;
import com.nextepisode.tmdb_service.service.TMDBWatchProvidersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/watch-providers")
@RestController
public class WatchProvidersController {

    private final TMDBWatchProvidersService watchProvidersClient;


    @Autowired
    public WatchProvidersController(TMDBWatchProvidersService restTMDBMovieClient) {
        this.watchProvidersClient = restTMDBMovieClient;

    }

    @GetMapping("/available-regions")
    public TMDBCountryListResponse getCountries() {
        return watchProvidersClient.getCountries();
    }

}
