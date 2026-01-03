package com.nextepisode.tmdb_service.tmdb.request;


import com.nextepisode.tmdb_service.enums.movie.MovieSortBy;
import com.nextepisode.tmdb_service.tmdb.TmdbConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDiscoverFilters {

    @Builder.Default
    private MovieSortBy sortBy = MovieSortBy.POPULARITY_DESC;

    @Builder.Default
    private Integer page = TmdbConstants.MIN_PAGE;

    @Builder.Default
    private String language = TmdbConstants.DEFAULT_LANGUAGE;

    @Builder.Default
    private Boolean includeAdult = TmdbConstants.DEFAULT_INCLUDE_ADULT;

    @Builder.Default
    private Boolean includeVideos = TmdbConstants.DEFAULT_INCLUDE_VIDEO;

    @Builder.Default
    private String region = TmdbConstants.DEFAULT_REGION;

    private Integer year;
    private Integer yearFrom;
    private Integer yearTo;
    private List<Integer> genres;
    private List<Integer> watchProviders;
    private List<String> keywords;
    private String certification;

    /***
     * Convert the list of movie genres ids to a string joined by comma ','
     * @return String
     */
    public String getGenreIdsAsString() {
        if (genres == null ) {
            return "";
        }
        return genres.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    /******
     * Convert the list of movie watch provider ids to a string joined by comma ','
     * @return String
     */
    public String getWatchProvidersIdsAsString() {
        if (watchProviders == null ) {
            return "";
        }
        return watchProviders.stream().map(String::valueOf).collect(Collectors.joining("|"));
    }

}

