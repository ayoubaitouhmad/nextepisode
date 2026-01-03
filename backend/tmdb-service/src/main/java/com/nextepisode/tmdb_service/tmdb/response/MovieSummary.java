package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.service.GenreService;
import com.nextepisode.tmdb_service.service.MovieService;
import com.nextepisode.tmdb_service.service.SearchService;
import com.nextepisode.tmdb_service.tmdb.common.BaseMovie;
import com.nextepisode.tmdb_service.tmdb.common.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class MovieSummary extends BaseMovie {

    @JsonProperty("genre_ids")
    private Integer[] genreIds;

    @JsonProperty(value = "genres", access = JsonProperty.Access.READ_ONLY)
    private List<Genre> genres = new ArrayList<>();

    @JsonProperty(value = "watch_providers", access = JsonProperty.Access.READ_ONLY)
    private CountryWatchProviderList watchProviders = null;

}