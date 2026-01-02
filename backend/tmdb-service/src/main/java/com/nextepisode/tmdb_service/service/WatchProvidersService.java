package com.nextepisode.tmdb_service.service;


import com.nextepisode.tmdb_service.exception.ErrorCode;
import com.nextepisode.tmdb_service.exception.TmdbApiException;
import com.nextepisode.tmdb_service.service.core.BaseService;
import com.nextepisode.tmdb_service.tmdb.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

import static com.nextepisode.tmdb_service.service.MovieService.MOVIE_BASE_PATH;

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


    /**
     * Fetches watch provider information for a specific movie.
     * This is a low-level method that returns null if providers are not found,
     * without throwing exceptions. Use this for optional enrichment scenarios.
     *
     * @param movieId the TMDB movie ID
     * @param region  the ISO 3166-1 country code (e.g., "US", "GB"), or null for all regions
     * @return Watching object containing provider information, or null if none found
     * @throws TmdbApiException only for API communication errors, not for missing data
     */
    public Watching fetchMovieWatchProviders(Long movieId, String region) {
        log.debug("Fetching watch providers for movie ID: {}, region: {}", movieId, region);

        try {
            MovieWatchProviderList providerList = tmdbClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(MOVIE_BASE_PATH + "/{id}/watch/providers")
                            .build(movieId))
                    .retrieve()
                    .body(MovieWatchProviderList.class);

            // Return null if no providers found (this is expected, not an error)
            if (providerList == null || providerList.getProviders().isEmpty()) {
                log.debug("No watch providers found for movie ID: {}", movieId);
                return null;
            }

            // Return all providers if no region specified
            if (region == null || region.isBlank()) {
                return providerList;
            }

            // Return region-specific providers (may return null if region has no providers)
            try {
                return getRegionSpecificProviders(providerList, region, movieId);
            } catch (TmdbApiException e) {
                // If region has no providers, return null instead of throwing
                if (e.getErrorCode() == ErrorCode.MOVIE_WATCH_PROVIDERS_NOT_FOUND) {
                    log.debug("No providers found for movie ID: {} in region: {}", movieId, region);
                    return null;
                }
                throw e; // Re-throw other exceptions (e.g., invalid region)
            }

        } catch (RestClientException e) {
            log.error("Failed to fetch watch providers for movie ID: {}", movieId, e);
            throw new TmdbApiException(
                    ErrorCode.API_COMMUNICATION_ERROR,
                    "Failed to fetch watch providers for movie: " + movieId,
                    e
            );
        }
    }

    /**
     * Extracts region-specific watch providers from the full provider list.
     */
    public Watching getRegionSpecificProviders(
            MovieWatchProviderList providerList,
            String region,
            Long movieId
    ) {
        CountryWatchProviderList regionProviders = providerList.getWatchProviderByRegion(region);

        if (regionProviders == null || !regionProviders.hasProviders()) {
            log.warn("No watch providers found for movie {} in region {}", movieId, region);
            throw new TmdbApiException(ErrorCode.REGION_INVALID_CODE, region);
        }

        return regionProviders;
    }
}
