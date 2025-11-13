package com.nextepisode.tmdb_service.enums.movie;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MovieSortBy {
    ORIGINAL_TITLE_ASC("original_title.asc", "Original Title (A-Z)"),
    ORIGINAL_TITLE_DESC("original_title.desc", "Original Title (Z-A)"),
    POPULARITY_ASC("popularity.asc", "Popularity (Low to High)"),
    POPULARITY_DESC("popularity.desc", "Popularity (High to Low)"),
    REVENUE_ASC("revenue.asc", "Revenue (Low to High)"),
    REVENUE_DESC("revenue.desc", "Revenue (High to Low)"),
    PRIMARY_RELEASE_DATE_ASC("primary_release_date.asc", "Release Date (Oldest First)"),
    PRIMARY_RELEASE_DATE_DESC("primary_release_date.desc", "Release Date (Newest First)"),
    TITLE_ASC("title.asc", "Title (A-Z)"),
    TITLE_DESC("title.desc", "Title (Z-A)"),
    VOTE_AVERAGE_ASC("vote_average.asc", "Rating (Low to High)"),
    VOTE_AVERAGE_DESC("vote_average.desc", "Rating (High to Low)"),
    VOTE_COUNT_ASC("vote_count.asc", "Vote Count (Low to High)"),
    VOTE_COUNT_DESC("vote_count.desc", "Vote Count (High to Low)");

    @JsonValue
    private final String value;
    private final String displayName;

    MovieSortBy(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }
    // Convert from string to enum
    public static MovieSortBy fromValue(String value) {

        if(value == null || value.isEmpty()) {
            return getDefault();
        }
        for (MovieSortBy sortBy : MovieSortBy.values()) {
            if (sortBy.value.equalsIgnoreCase(value)) {
                return sortBy;
            }
        }

        throw new IllegalArgumentException("Invalid sort_by value: " + value);

    }

    // Get default sort option
    public static MovieSortBy getDefault() {
        return VOTE_AVERAGE_DESC;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
