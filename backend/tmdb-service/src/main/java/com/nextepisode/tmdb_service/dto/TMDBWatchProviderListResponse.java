package com.nextepisode.tmdb_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class TMDBWatchProviderListResponse {

    @Builder.Default
    public int total = 0;

//    @Builder.Default
//    public Instant storedAt =  Instant.now();

    @JsonProperty("results")
    public List<TMDBWatchProvider> providers;
}
