package com.nextepisode.tmdb_service.service.utll;


import com.nextepisode.tmdb_service.tmdb.TmdbConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;


@Slf4j
public class ValidationUtils {

    /**
     * Validates and normalizes page number.
     */
    public static Integer validatePage(Integer page) {
        if (page == null || page < TmdbConstants.MIN_PAGE) {
            return TmdbConstants.MIN_PAGE;
        }
        if (page > TmdbConstants.MAX_PAGE) {
            log.warn("Page number {} exceeds maximum {}, capping to maximum", page, TmdbConstants.MAX_PAGE);
            return TmdbConstants.MAX_PAGE;
        }
        return page;
    }

    /**
     * Validates and normalizes language code.
     */
    public static String validateLanguage(String language) {
        return Optional.ofNullable(language)
                .filter(lang -> !lang.isBlank())
                .orElse("en-US");
    }

    /**
     * Validates time window for trending movies.
     */
    public static String validateTimeWindow(String timeWindow) {
        if (timeWindow == null || timeWindow.isBlank()) {
            return "week";
        }

        String normalized = timeWindow.toLowerCase();
        if (!normalized.equals("day") && !normalized.equals("week")) {
            log.warn("Invalid time window '{}', defaulting to 'week'", timeWindow);
            return "week";
        }

        return normalized;
    }

}
