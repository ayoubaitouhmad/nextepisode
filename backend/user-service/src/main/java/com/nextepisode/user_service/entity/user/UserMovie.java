package com.nextepisode.user_service.entity.user;

import com.nextepisode.user_service.entity.movie.Movie;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userUsername")
    @JoinColumn(name = "user_username", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "is_favorite")
    private boolean isFavorite = false;

    @Column(name = "in_watchlist")
    private boolean inWatchlist = false;

    @Column(name = "watched")
    private boolean watched = false;

    @Column(name = "watched_at")
    private Instant watchedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Convenience constructor
    public UserMovie(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
        this.id = new UserMovieId(user.getUsername(), movie.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserMovie that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}