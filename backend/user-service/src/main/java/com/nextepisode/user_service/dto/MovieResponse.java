package com.nextepisode.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
public class MovieResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("watched_at")
    private Instant watchedAt;


    public MovieResponse(Long id, String title, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;

    }


    public MovieResponse(Long id, String title, Instant createdAt , Instant watchedAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.watchedAt = watchedAt;
    }



}
