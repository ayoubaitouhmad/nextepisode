package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.common.BaseMovie;
import com.nextepisode.tmdb_service.tmdb.common.Genre;
import com.nextepisode.tmdb_service.tmdb.common.Language;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MovieDetails extends BaseMovie {

    @JsonProperty("belongs_to_collection")
    private Object belongsToCollection;  // Can create a Collection class later

    @JsonProperty("budget")
    private Long budget;

    @JsonProperty("genres")
    private List<Genre> genres;

    @JsonProperty("homepage")
    private String homepage;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("origin_country")
    private List<String> originCountry;

    @JsonProperty("production_companies")
    private List<Object> productionCompanies;

    @JsonProperty("production_countries")
    private List<Object> productionCountries;

    @JsonProperty("revenue")
    private Long revenue;

    @JsonProperty("runtime")
    private Integer runtime;

    @JsonProperty("spoken_languages")
    private List<Language> spokenLanguages;

    @JsonProperty("status")
    private String status;

    @JsonProperty("tagline")
    private String tagline;
}