package com.nextepisode.user_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres")  // Separate table for genre definitions
@Getter
@Setter
@NoArgsConstructor
public class Genre {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private List<Movie> movies = new ArrayList<>();

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Avoid infinite recursion in toString, equals, hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre genre)) return false;
        return id != null && id.equals(genre.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}