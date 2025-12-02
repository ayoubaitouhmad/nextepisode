package com.nextepisode.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieListResponse {
    @JsonProperty("page")
    private Integer page;

    @JsonProperty("results")
    private List<MovieResponse> results;

    @JsonProperty("total_pages")
    private Long totalPages;

    @JsonProperty("total_results")
    private Long totalResults;
}
