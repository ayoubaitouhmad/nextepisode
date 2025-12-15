package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.common.TMDBCountry;
import lombok.Data;

import java.util.List;

@Data
public class TMDBCountryListResponse {

    @JsonProperty("results")
    public List<TMDBCountry> results;

}
