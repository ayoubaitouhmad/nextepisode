package com.nextepisode.tmdb_service.dto.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class TMDBLanguageListResponse {

    @Builder.Default
    public Integer total = 0;

    @Builder.Default
    public Instant storedAt = Instant.now();

    @JsonProperty("languages")
    public List<TMDBLanguage> languages;

}
