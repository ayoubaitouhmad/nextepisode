package com.nextepisode.user_service.events.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieEnrichedStatusEvent implements Serializable {
    private String userId;
    private Long movieId;
    private String category; // "FAVORITE", "WATCHED", "WATCHLIST"
    private String action; // "ADD", "REMOVED"

    private Long id;
    private Boolean adult;
    private String title;
    private String originalTitle;
    private String overview;
    private String originalLanguage;
    private String posterPath;
    private String backdropPath;
    private String releaseDate;
    private String homepage;
    private Double popularity;
    private String status;
    private Integer voteCount;
    private List<String> genres;
}