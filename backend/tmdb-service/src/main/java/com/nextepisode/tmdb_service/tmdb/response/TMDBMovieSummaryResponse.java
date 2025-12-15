package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.service.TMDBImageService;
import lombok.Data;

import java.util.List;

@Data
public class TMDBMovieSummaryResponse {

    @JsonProperty("adult")
    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("genre_ids")
    private List<Integer> genreIds;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("original_language")
    private String original_language;

    @JsonProperty("original_title")
    private String original_title;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("popularity")
    private Double popularity;

    @JsonProperty("poster_path")
    private String poster_path;

    @JsonProperty("release_date")
    private String release_date;

    @JsonProperty("title")
    private String title;

    @JsonProperty("video")
    private boolean video;

    @JsonProperty("vote_average")
    private Double vote_average;

    @JsonProperty("vote_count")
    private Integer vote_count;

    public void setPoster_path(String poster_path) {
        this.poster_path = TMDBImageService.getTMDBImageService().setPath(poster_path).get();
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = TMDBImageService.getTMDBImageService().setPath(backdropPath).get();
    }


}
