package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.tmdb.response.CountryList;
import com.nextepisode.tmdb_service.tmdb.response.WatchProviderList;
import com.nextepisode.tmdb_service.service.WatchProvidersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(ApiPaths.API_V1 +"/watch-providers")
@RestController
public class WatchProvidersController {

    private final WatchProvidersService watchProvidersClient;


    @Autowired
    public WatchProvidersController(WatchProvidersService restTMDBMovieClient) {
        this.watchProvidersClient = restTMDBMovieClient;

    }

    @GetMapping("/available-regions")
    public CountryList getCountries() {
        return watchProvidersClient.getCountries();
    }


    @GetMapping("/movie")
    public WatchProviderList watchProvidersForMovies(
            @RequestParam(required = false) String region
    ) {
        return watchProvidersClient.getWatchProvidersForMovies(region);
    }

    @GetMapping("/tv-show")
    public WatchProviderList watchProvidersForTvSows(
            @RequestParam(required = false) String region
    ) {
        return watchProvidersClient.getWatchProvidersForTvShows(region);
    }


}
