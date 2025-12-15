package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.tmdb.response.TMDBLanguageListResponse;
import com.nextepisode.tmdb_service.service.TMDBConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(ApiPaths.API_V1 + "/configuration")
@RestController
public class ConfigurationController {

    private final TMDBConfigurationService tmdbConfigurationService;

    public ConfigurationController(TMDBConfigurationService tmdbConfigurationService) {
        this.tmdbConfigurationService = tmdbConfigurationService;
    }

    @GetMapping("/languages")
    public TMDBLanguageListResponse moviesGenres() {
        return tmdbConfigurationService.getLanguages();
    }
}
