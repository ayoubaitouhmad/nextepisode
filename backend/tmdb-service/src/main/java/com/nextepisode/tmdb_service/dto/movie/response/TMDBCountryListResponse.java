package com.nextepisode.tmdb_service.dto.movie.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.dto.movie.TMDBCountry;
import lombok.Data;

import java.util.List;

@Data
public class TMDBCountryListResponse {

    @JsonProperty("results")
    public List<TMDBCountry> results;

}
