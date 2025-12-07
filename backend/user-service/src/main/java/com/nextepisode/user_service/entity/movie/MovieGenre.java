package com.nextepisode.user_service.entity.movie;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie_genres")  // Separate table for genre definitions
@Getter
@Setter
@NoArgsConstructor
public class MovieGenre {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private List<Movie> movies = new ArrayList<>();

    public MovieGenre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Avoid infinite recursion in toString, equals, hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieGenre genre)) return false;
        return id != null && id.equals(genre.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}