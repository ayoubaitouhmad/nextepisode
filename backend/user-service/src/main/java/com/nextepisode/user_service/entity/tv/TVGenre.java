package com.nextepisode.user_service.entity.tv;

import com.nextepisode.user_service.entity.movie.Movie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tv_genres")  // Separate table for genre definitions
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TVGenre {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private List<Tv> tvs = new ArrayList<>();

}