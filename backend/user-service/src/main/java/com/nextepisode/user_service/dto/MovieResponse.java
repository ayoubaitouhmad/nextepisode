package com.nextepisode.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.user_service.entity.movie.Movie;
import com.nextepisode.user_service.entity.movie.MovieGenre;
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
public class MovieResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

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


    public MovieResponse( Movie movie , Instant createdAt) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.createdAt = createdAt;
        this.releaseDate = movie.getReleaseDate();
        this.genres =buildGenres(movie.getGenres());
        this.posterPath = movie.getPosterPath();
    }

    public MovieResponse( Movie movie , Instant createdAt , Instant watchedAt) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.createdAt = createdAt;
        this.watchedAt = watchedAt;
        this.releaseDate = movie.getReleaseDate();
        this.genres =buildGenres(movie.getGenres());
        this.posterPath = movie.getPosterPath();
    }


    private List<Genre> buildGenres(List<MovieGenre> genres) {

        return genres.stream().map(
                genre ->
                        Genre.builder().id(genre.getId()).name(genre.getName()).build()
        ).toList();
    }

}
