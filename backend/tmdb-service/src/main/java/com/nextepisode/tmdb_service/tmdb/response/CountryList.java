package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.common.Country;
import lombok.Data;

import java.util.List;

@Data
public class CountryList {

    @JsonProperty("results")
    public List<Country> results;

}
