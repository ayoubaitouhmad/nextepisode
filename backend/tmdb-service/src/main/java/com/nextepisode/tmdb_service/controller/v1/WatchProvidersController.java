package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.tmdb.response.TMDBCountryListResponse;
import com.nextepisode.tmdb_service.tmdb.response.TMDBWatchProviderListResponse;
import com.nextepisode.tmdb_service.service.TMDBWatchProvidersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(ApiPaths.API_V1 +"/watch-providers")
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


    @GetMapping("/movie")
    public TMDBWatchProviderListResponse watchProvidersForMovies(
            @RequestParam(required = false) String region
    ) {
        return watchProvidersClient.getWatchProvidersForMovies(region);
    }

    @GetMapping("/tv-show")
    public TMDBWatchProviderListResponse watchProvidersForTvSows(
            @RequestParam(required = false) String region
    ) {
        return watchProvidersClient.getWatchProvidersForTvShows(region);
    }


}
