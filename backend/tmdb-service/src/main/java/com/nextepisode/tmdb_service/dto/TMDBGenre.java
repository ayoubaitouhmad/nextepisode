package com.nextepisode.tmdb_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TMDBGenre {

    @JsonProperty("id")
    private Number id;
    @JsonProperty("name")
    private String name;
}
