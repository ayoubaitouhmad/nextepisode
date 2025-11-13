package com.nextepisode.tmdb_service.service;

import com.nextepisode.tmdb_service.dto.movie.response.TMDBCountryListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class TMDBWatchProvidersService extends BaseService {


    public TMDBWatchProvidersService(RestClient TMDBClient) {
        super(TMDBClient);
    }



    @Cacheable("countries")
    public TMDBCountryListResponse getCountries() {
        try {
            return TMDBClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/watch/providers/regions")
                            .queryParam("language", "en-US")
                            .build()
                    )
                    .retrieve()
                    .body(TMDBCountryListResponse.class);

        } catch (Exception e) {
            log.error("Failed to get countries: ", e);
            throw new RuntimeException("Failed to get countries: ", e);
        }
    }
}
