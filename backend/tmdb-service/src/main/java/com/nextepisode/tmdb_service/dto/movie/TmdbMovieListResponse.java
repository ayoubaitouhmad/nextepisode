package com.nextepisode.tmdb_service.dto.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbMovieListResponse {

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("results")
    private List<TMDBMovieSummary> results;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("total_results")
    private Integer totalResults;


}
