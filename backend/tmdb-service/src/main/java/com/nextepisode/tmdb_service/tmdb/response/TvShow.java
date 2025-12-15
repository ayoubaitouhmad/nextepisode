package com.nextepisode.tmdb_service.tmdb.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.common.Media;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TvShow extends Media {
    @JsonProperty("first_air_date")
    private LocalDate firstAirDate;

    @JsonProperty("in_production")
    private boolean inProduction;

    @JsonProperty("last_air_date")
    private LocalDate lastAirDate;

    @JsonProperty("original_name")
    private String originalName;

    @JsonProperty("type")
    private String type;

    public static TvShow buildFromMedia(Media media) {
        return TvShow.builder()
                // From IdElement
                .id(media.getId())
                // From Media
                .adult(media.isAdult())
                .backdropPath(media.getBackdropPath())
                .homepage(media.getHomepage())
                .originalLanguage(media.getOriginalLanguage())
                .overview(media.getOverview())
                .popularity(media.getPopularity())
                .posterPath(media.getPosterPath())
                .status(media.getStatus())
                .tagline(media.getTagline())
                .voteAverage(media.getVoteAverage())
                .voteCount(media.getVoteCount())
                .build();
    }
}
