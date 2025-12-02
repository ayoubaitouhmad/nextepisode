package com.nextepisode.user_service.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserMovieId implements Serializable {
    private String userUsername;
    private Long movieId;



}
