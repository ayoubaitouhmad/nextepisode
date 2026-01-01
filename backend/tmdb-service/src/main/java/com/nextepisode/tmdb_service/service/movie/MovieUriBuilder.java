package com.nextepisode.tmdb_service.service.movie;


import com.nextepisode.tmdb_service.tmdb.request.MovieDiscoverFilters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

/**
 * Component responsible for building URIs for movie discovery API calls.
 * Handles complex parameter combinations and validation.
 */
@Slf4j
@Component
public class MovieUriBuilder {

    private static final String DISCOVER_PATH = "/discover/movie";
    private static final int MIN_PAGE = 1;
    private static final int MAX_PAGE = 500;

    /**
     * Builds a complete URI for the discover movies endpoint with all filters applied.
     *
     * @param uriBuilder Spring's URI builder
     * @param filters the discovery filters
     * @return fully constructed URI
     */
    public URI buildDiscoverUri(UriBuilder uriBuilder, MovieDiscoverFilters filters) {
        uriBuilder.path(DISCOVER_PATH);

        addBaseParameters(uriBuilder, filters);
        addYearFilters(uriBuilder, filters);
        addWatchProviderFilters(uriBuilder, filters);
        addGenreFilters(uriBuilder, filters);

        URI uri = uriBuilder.build();
        log.debug("Built discover URI: {}", uri);
        return uri;
    }

    /**
     * Adds base query parameters (sort, page, language, etc.).
     */
    private void addBaseParameters(UriBuilder uriBuilder, MovieDiscoverFilters filters) {
        uriBuilder
                .queryParam("sort_by", filters.getSortBy())
                .queryParam("page", validatePage(filters.getPage()))
                .queryParam("language", validateLanguage(filters.getLanguage()))
                .queryParam("include_adult", filters.getIncludeAdult());

        // Add watch region if specified
        if (filters.getRegion() != null && !filters.getRegion().isBlank()) {
            uriBuilder.queryParam("watch_region", filters.getRegion());
        }
    }

    /**
     * Adds year-based filters (specific year or year range).
     */
    private void addYearFilters(UriBuilder uriBuilder, MovieDiscoverFilters filters) {
        // Specific year takes precedence over range
        if (filters.getYear() != null) {
            uriBuilder.queryParam("primary_release_year", filters.getYear());
            return;
        }

        // Add year range if specified
        if (filters.getYearFrom() != null) {
            uriBuilder.queryParam("primary_release_date.gte", filters.getYearFrom() + "-01-01");
        }

        if (filters.getYearTo() != null) {
            uriBuilder.queryParam("primary_release_date.lte", filters.getYearTo() + "-12-31");
        }
    }

    /**
     * Adds watch provider filters.
     */
    private void addWatchProviderFilters(UriBuilder uriBuilder, MovieDiscoverFilters filters) {
        if (filters.getWatchProviders() != null && !filters.getWatchProviders().isEmpty()) {
            String providerIds = filters.getWatchProvidersIdsAsString();
            if (providerIds != null && !providerIds.isBlank()) {
                uriBuilder.queryParam("with_watch_providers", providerIds);
            }
        }
    }

    /**
     * Adds genre filters.
     */
    private void addGenreFilters(UriBuilder uriBuilder, MovieDiscoverFilters filters) {
        if (filters.getGenres() != null && !filters.getGenres().isEmpty()) {
            String genreIds = filters.getGenreIdsAsString();
            if (genreIds != null && !genreIds.isBlank()) {
                uriBuilder.queryParam("with_genres", genreIds);
            }
        }
    }

    /**
     * Validates and normalizes page number.
     */
    private Integer validatePage(Integer page) {
        if (page == null || page < MIN_PAGE) {
            return MIN_PAGE;
        }
        if (page > MAX_PAGE) {
            log.warn("Page number {} exceeds maximum {}, capping to maximum", page, MAX_PAGE);
            return MAX_PAGE;
        }
        return page;
    }

    /**
     * Validates and normalizes language code.
     */
    private String validateLanguage(String language) {
        if (language == null || language.isBlank()) {
            return "en-US";
        }
        return language;
    }
}