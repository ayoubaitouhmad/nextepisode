package com.nextepisode.tmdb_service.dto.movie.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.dto.movie.TMDBGenre;
import lombok.Data;

import java.util.List;

@Data
public class TMDBMovieGenres {

    @JsonProperty("genres")
    public List<TMDBGenre> genres;
}
