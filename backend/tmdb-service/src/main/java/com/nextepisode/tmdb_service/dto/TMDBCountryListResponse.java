package com.nextepisode.tmdb_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TMDBCountryListResponse {

    @JsonProperty("results")
    public List<TMDBCountry> results;

}
