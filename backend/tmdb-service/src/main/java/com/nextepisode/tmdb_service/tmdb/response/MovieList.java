package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.core.AbstractJsonMapping;
import lombok.Data;

import java.util.List;

@Data
public class MovieList extends AbstractJsonMapping {

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("results")
    private List<MovieSummary> results;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("total_results")
    private Integer totalResults;

}
