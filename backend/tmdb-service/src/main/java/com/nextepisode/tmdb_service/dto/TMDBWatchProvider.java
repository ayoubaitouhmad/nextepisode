package com.nextepisode.tmdb_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class TMDBWatchProvider {

    @JsonProperty("provider_id")
    private Integer providerId;

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("logo_path")
    private String logoPath;

    @JsonProperty("display_priority")
    private Integer displayPriority;

    @JsonProperty("display_priorities")
    private Map<String, Integer> displayPriorities;


    public Map<String, Integer> getDisplayPriorities() {
        return  new HashMap<>();
    }
}
