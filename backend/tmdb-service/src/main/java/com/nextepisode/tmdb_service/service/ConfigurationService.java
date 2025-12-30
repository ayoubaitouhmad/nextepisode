package com.nextepisode.tmdb_service.service;


import com.nextepisode.tmdb_service.config.CacheConfig;
import com.nextepisode.tmdb_service.enums.movie.MovieSortBy;
import com.nextepisode.tmdb_service.service.utll.DateHelper;
import com.nextepisode.tmdb_service.tmdb.common.Language;
import com.nextepisode.tmdb_service.tmdb.response.LanguageList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ConfigurationService extends BaseService {

    private final DateHelper dateHelper;

    public ConfigurationService(RestClient tmdbClient) {
        super(tmdbClient);
        this.dateHelper = new DateHelper();
    }


    @Cacheable(CacheConfig.TMDB_API_LANGUAGES)
    public LanguageList getLanguages() {
        log.info("Start getting tmdb api languages");
        try {
            List<Language> languages = tmdbClient.get().uri(uriBuilder -> uriBuilder
                    .path("/configuration/languages")
                    .queryParam("language", "en-US")
                    .build()).retrieve().body(new ParameterizedTypeReference<List<Language>>() {
                                              }
            );

            return LanguageList.builder().languages(languages).total(languages.size()).build();

        } catch (Exception e) {
            log.error("Failed to get languages: ", e);
            throw new RuntimeException("Failed to get languages: ", e);
        }
    }

    @Cacheable(CacheConfig.YEARS)
    public List<Integer> getYearsSequence() {
        return dateHelper.getYearsSequence();
    }


    public Map<String, String> getSortByOptions() {
        Map<String, String> list = new HashMap<>();
        for (MovieSortBy rt : MovieSortBy.values()) {
            list.put( rt.name() , rt.getDisplayName());
        }
        return list;
    }
}
