package com.nextepisode.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    private Long id;  // TMDB ID - not auto-generated

    @Column(nullable = false)
    private String title;

    private String posterPath;

    private Instant releaseDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "movie_genre",  // Dedicated join table
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    // Helper methods for managing the bidirectional relationship
    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getMovies().add(this);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getMovies().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie movie)) return false;
        return id != null && id.equals(movie.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}