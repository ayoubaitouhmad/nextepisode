package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.common.TMDBWatchProvider;
import lombok.Builder;
import lombok.Data;

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
