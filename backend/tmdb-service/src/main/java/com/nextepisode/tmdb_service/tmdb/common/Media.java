package com.nextepisode.tmdb_service.tmdb.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.core.IdElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class Media extends IdElement {

    @JsonProperty("adult")
    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("homepage")
    private String homepage;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("popularity")
    private Double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("status")
    private String status;

    @JsonProperty("tagline")
    private String tagline;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("vote_count")
    private Integer voteCount;

    @JsonProperty("media_type")
    private String mediaType;

    @JsonProperty("genres")
    private List<Genre> genres;

}