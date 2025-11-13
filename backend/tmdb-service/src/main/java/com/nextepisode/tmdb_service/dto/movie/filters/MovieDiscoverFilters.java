package com.nextepisode.tmdb_service.dto.movie.filters;


import com.nextepisode.tmdb_service.enums.movie.MovieSortBy;
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
    private Integer page = 1;

    @Builder.Default
    private String language = "en-US";

    @Builder.Default
    private Boolean includeAdult = false;

    @Builder.Default
    private Boolean includeVideos = false;

    @Builder.Default
    private String watchRegion = "US";

    private Integer year;
    private Integer yearFrom;
    private Integer yearTo;
    private List<Integer> genres;
    private List<Integer> watchProviders;


    public String getGenreIdsAsString() {
        if (genres == null ) {
            return "";
        }
        return genres.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public String getWatchProvidersIdsAsString() {
        if (watchProviders == null ) {
            return "";
        }
        return watchProviders.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}

