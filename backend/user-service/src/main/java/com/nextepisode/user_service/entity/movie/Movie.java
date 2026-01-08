package com.nextepisode.user_service.entity.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate releaseDate;
    private Boolean adult;
    private String originalTitle;
    @Column(length = 1000)
    private String overview;
    private String originalLanguage;
    private String backdropPath;
    private String homepage;
    private Double popularity;
    private String status;
    private Integer voteCount;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<MovieGenre> genres = new ArrayList<>();


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