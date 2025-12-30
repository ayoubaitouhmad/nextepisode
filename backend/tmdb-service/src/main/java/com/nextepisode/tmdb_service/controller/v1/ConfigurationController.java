package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.service.ConfigurationService;
import com.nextepisode.tmdb_service.tmdb.enums.ContentRuntime;
import com.nextepisode.tmdb_service.tmdb.response.LanguageList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping(ApiPaths.API_V1 + "/configuration")
@RestController
public class ConfigurationController {

    private final ConfigurationService tmdbConfigurationService;

    public ConfigurationController(ConfigurationService tmdbConfigurationService) {
        this.tmdbConfigurationService = tmdbConfigurationService;
    }

    @GetMapping("/languages")
    public LanguageList moviesGenres() {
        return tmdbConfigurationService.getLanguages();
    }


    @GetMapping("/years")
    public List<Integer> getYears() {
        return tmdbConfigurationService.getYearsSequence();
    }

    @GetMapping("/runtimes")
    public Map<String, Integer> vdfs() {
        Map<String, Integer> list = new HashMap<>();

        for (ContentRuntime rt : ContentRuntime.values()) {
            list.put(rt.getName(), rt.getRuntime());
        }

        return list;
    }

    @GetMapping("/sorting")
    public Map<String, String> sorting() {
        return tmdbConfigurationService.getSortByOptions();
    }
}
