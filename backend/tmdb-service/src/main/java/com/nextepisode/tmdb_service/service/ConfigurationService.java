package com.nextepisode.tmdb_service.service;


import com.nextepisode.tmdb_service.config.CacheConfig;
import com.nextepisode.tmdb_service.tmdb.common.Language;
import com.nextepisode.tmdb_service.tmdb.response.LanguageList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class ConfigurationService extends BaseService {


    public ConfigurationService(RestClient tmdbClient) {
        super(tmdbClient);
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


}
