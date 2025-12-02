package com.nextepisode.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMovie {

    @EmbeddedId
    private UserMovieId id = new UserMovieId();

    @ManyToOne
    @MapsId("userUsername")
    private User user;

    @ManyToOne
    @MapsId("movieId")
    private Movie movie;

    private boolean isFavorite;
    private boolean inWatchlist;
    private boolean watched;

    private Instant watchedAt;
    private Instant createdAt = Instant.now();

}
