package com.nextepisode.tmdb_service.service;


import com.nextepisode.tmdb_service.config.CacheConfig;
import com.nextepisode.tmdb_service.dto.configuration.TMDBLanguage;
import com.nextepisode.tmdb_service.dto.configuration.TMDBLanguageListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class TMDBConfigurationService extends BaseService {


    public TMDBConfigurationService(RestClient TMDBClient) {
        super(TMDBClient);
    }


    @Cacheable(CacheConfig.TMDB_API_LANGUAGES)
    public TMDBLanguageListResponse getLanguages() {
        log.info("Start getting tmdb api languages");
        try {
            List<TMDBLanguage> languages = TMDBClient.get().uri(uriBuilder -> uriBuilder
                    .path("/configuration/languages")
                    .queryParam("language", "en-US")
                    .build()).retrieve().body(new ParameterizedTypeReference<List<TMDBLanguage>>() {
                                              }
            );

            return TMDBLanguageListResponse.builder().languages(languages).total(languages.size()).build();

        } catch (Exception e) {
            log.error("Failed to get languages: ", e);
            throw new RuntimeException("Failed to get languages: ", e);
        }
    }


}
