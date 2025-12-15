package com.nextepisode.tmdb_service.service;


import com.nextepisode.tmdb_service.tmdb.response.CountryList;
import com.nextepisode.tmdb_service.tmdb.response.WatchProviderList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
public class WatchProvidersService extends BaseService {


    public WatchProvidersService(RestClient tmdbClient) {
        super(tmdbClient);
    }


    @Cacheable("countries")
    public CountryList getCountries() {
        try {
            return tmdbClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/watch/providers/regions")
                            .queryParam("language", "en-US")
                            .build()
                    )
                    .retrieve()
                    .body(CountryList.class);

        } catch (Exception e) {
            log.error("Failed to get countries: ", e);
            throw new RuntimeException("Failed to get countries: ", e);
        }
    }

    public WatchProviderList getWatchProvidersForMovies(String region) {
        log.info("Start getting watch providers for movies with region={}", region);
        try {
            WatchProviderList tmdbWatchProviderListResponse = tmdbClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/watch/providers/movie")
                            .queryParam("language", "en-US")
                            .queryParamIfPresent("watch_region", Optional.ofNullable(region))
                            .build()
                    )
                    .retrieve()
                    .body(WatchProviderList.class);

            tmdbWatchProviderListResponse.getProviders().sort(
                    (o1, o2) -> o1.getDisplayPriority().compareTo(o2.getDisplayPriority())
            );

            return WatchProviderList.builder()
                    .providers(
                            tmdbWatchProviderListResponse.getProviders()
                    )
                    .total(tmdbWatchProviderListResponse.getProviders().size())
                    .build();

        } catch (Exception e) {
            log.error("getting watch providers for movies with region:{} ", region, e);
            throw new RuntimeException("Failed to get countries: ", e);
        }
    }

    public WatchProviderList getWatchProvidersForTvShows(String region) {
        log.info("Start getting watch providers for movies with region={}", region);
        try {
            WatchProviderList tmdbWatchProviderListResponse = tmdbClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/watch/providers/tv")
                            .queryParam("language", "en-US")
                            .queryParamIfPresent("watch_region", Optional.ofNullable(region))
                            .build()
                    )
                    .retrieve()
                    .body(WatchProviderList.class);

            tmdbWatchProviderListResponse.getProviders().sort(
                    (o1, o2) -> o1.getDisplayPriority().compareTo(o2.getDisplayPriority())
            );

            return WatchProviderList.builder()
                    .providers(
                            tmdbWatchProviderListResponse.getProviders()
                    )
                    .total(tmdbWatchProviderListResponse.getProviders().size())
                    .build();

        } catch (Exception e) {
            log.error("getting watch providers for movies with region:{} ", region, e);
            throw new RuntimeException("Failed to get countries: ", e);
        }
    }

}
