package com.nextepisode.tmdb_service.service.movie;

import com.nextepisode.tmdb_service.exception.TmdbApiException;
import com.nextepisode.tmdb_service.service.WatchProvidersService;
import com.nextepisode.tmdb_service.tmdb.response.CountryWatchProviderList;
import com.nextepisode.tmdb_service.tmdb.response.MovieList;
import com.nextepisode.tmdb_service.tmdb.response.MovieSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovieWatchProviderEnricher {

    private final WatchProvidersService watchProvidersService;

    /**
     * Enriches a list of movies with watch provider information.
     * This method gracefully handles cases where providers are not available
     * for individual movies, setting them to null instead of failing.
     *
     * @param movieList the list of movies to enrich
     * @param region    the ISO 3166-1 country code for regional providers
     */
    public void enrichMoviesWithWatchProviders(MovieList movieList, String region) {
        if (movieList == null || movieList.getResults() == null || movieList.getResults().isEmpty()) {
            log.debug("No movies to enrich with watch providers");
            return;
        }

        log.info("Enriching {} movies with watch providers for region: {}",
                movieList.getResults().size(), region != null ? region : "all");

        int enrichedCount = 0;
        int missingCount = 0;

        for (MovieSummary movie : movieList.getResults()) {
            try {
                log.debug("Fetching watch providers for movie ID: {} - '{}'",
                        movie.getId(), movie.getTitle());

                // Use fetch method (returns null if not found, doesn't throw)
                CountryWatchProviderList providers =
                        (CountryWatchProviderList) watchProvidersService.fetchMovieWatchProviders(movie.getId(), region);

                movie.setWatchProviders(providers);

                if (providers == null) {
                    log.debug("No watch providers found for movie ID: {} - '{}'",
                            movie.getId(), movie.getTitle());
                    missingCount++;
                } else {
                    int rentCount = providers.getRent() != null ? providers.getRent().size() : 0;
                    int buyCount = providers.getBuy() != null ? providers.getBuy().size() : 0;

                    int totalProviders = rentCount + buyCount;

                    log.debug("Attached {} providers (rent: {}, buy: {}) to movie ID: {} - '{}'",
                            totalProviders, rentCount, buyCount,
                            movie.getId(), movie.getTitle());
                    enrichedCount++;
                }

            } catch (TmdbApiException e) {
                // Log the error but continue processing other movies
                log.warn("Failed to fetch watch providers for movie ID: {} - '{}': {}",
                        movie.getId(), movie.getTitle(), e.getMessage());
                movie.setWatchProviders(null);
                missingCount++;
            }
        }

        log.info("Watch provider enrichment complete: {} movies enriched, {} without providers",
                enrichedCount, missingCount);
    }
}
