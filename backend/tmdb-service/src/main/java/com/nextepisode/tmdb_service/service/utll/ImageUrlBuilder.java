package com.nextepisode.tmdb_service.service.utll;

import org.springframework.stereotype.Service;

public final class ImageUrlBuilder {

    private static final String BASE_URL = "https://image.tmdb.org/t/p";

    // Size constants
    public static final String W_92 = "w92";
    public static final String W_154 = "w154";
    public static final String W_185 = "w185";
    public static final String W_342 = "w342";
    public static final String W_500 = "w500";
    public static final String W_780 = "w780";
    public static final String ORIGINAL = "original";

    private ImageUrlBuilder() {} // Prevent instantiation

    /**
     * Builds full image URL from TMDB path
     */
    public static String buildUrl(String path) {
        return buildUrl(path, ORIGINAL);
    }

    public static String buildUrl(String path, String size) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        String safeSize = (size == null || size.isEmpty()) ? ORIGINAL : size;

        return BASE_URL + "/" + safeSize + "/" + cleanPath;
    }
}