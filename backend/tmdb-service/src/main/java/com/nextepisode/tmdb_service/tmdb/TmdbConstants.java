package com.nextepisode.tmdb_service.tmdb;

import com.nextepisode.tmdb_service.enums.movie.MovieSortBy;

public class TmdbConstants {
    private TmdbConstants() {}

    public static final int MIN_PAGE = 1;
    public static final int MAX_PAGE = 500;
    public static final String DEFAULT_LANGUAGE = "en-US";
    public static final String DEFAULT_TIME_WINDOW = "week";
    public static final String DEFAULT_REGION = "US";
    public static final Boolean DEFAULT_INCLUDE_ADULT = false;
    public static final Boolean DEFAULT_INCLUDE_VIDEO = false;
    public static final MovieSortBy DEFAULT_MOVIE_SORT_BY = MovieSortBy.getDefault();

}
