package com.nextepisode.user_service.entity.tv;

import com.nextepisode.user_service.entity.movie.MovieGenre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tvs")
@Getter
@Setter
@NoArgsConstructor
public class Tv {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private String overview;
    private String posterPath;
    private Instant releaseDate;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tv_genre",
            joinColumns = @JoinColumn(name = "tv_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<TVGenre> genres = new ArrayList<>();




    public void addGenre(TVGenre genre) {
        this.genres.add(genre);
        genre.getTvs().add(this);
    }

    public void removeGenre(TVGenre genre) {
        this.genres.remove(genre);
        genre.getTvs().remove(this);
    }
}
