package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.common.TMDBGenre;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class TMDBGenreListResponse {
    @Builder.Default
    public Integer total = 0;

    @Builder.Default
    public Instant storedAt =  Instant.now();


    @JsonProperty("genres")
    public List<TMDBGenre> genres;
}
