package com.nextepisode.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.user_service.entity.movie.Movie;
import com.nextepisode.user_service.entity.movie.MovieGenre;
import com.nextepisode.user_service.entity.tv.TVGenre;
import com.nextepisode.user_service.entity.tv.Tv;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TvResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("watched_at")
    private Instant watchedAt;

    @JsonProperty("release_date")
    private Instant releaseDate;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("genres")
    private List<Genre> genres;


    public TvResponse(Tv tv , Instant createdAt) {
        this.id = tv.getId();
        this.name = tv.getName();
        this.createdAt = createdAt;
        this.releaseDate = tv.getReleaseDate();
        this.genres =buildGenres(tv.getGenres());
        this.posterPath = tv.getPosterPath();
    }

    public TvResponse(Tv tv , Instant createdAt , Instant watchedAt) {
        this.id = tv.getId();
        this.name = tv.getName();
        this.createdAt = createdAt;
        this.releaseDate = tv.getReleaseDate();
        this.genres =buildGenres(tv.getGenres());
        this.posterPath = tv.getPosterPath();
        this.watchedAt = watchedAt;
    }


    private List<Genre> buildGenres(List<TVGenre> genres) {

        return genres.stream().map(
                genre ->
                        Genre.builder().id(genre.getId()).name(genre.getName()).build()
        ).toList();
    }

}
