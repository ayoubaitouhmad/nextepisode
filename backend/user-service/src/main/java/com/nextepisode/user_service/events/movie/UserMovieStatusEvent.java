package com.nextepisode.user_service.events.movie;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMovieStatusEvent implements Serializable {
    private String userId;
    private Long movieId;
    private String category; // "FAVORITE", "WATCHED", "WATCHLIST"
    private String action; // "ADD", "REMOVED"
}