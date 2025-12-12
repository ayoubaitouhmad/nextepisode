package com.nextepisode.tmdb_service.dto.movie.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TMDBMovieGenres {

    @JsonProperty("genres")
    public List<TMDBGenre> genres;
}
