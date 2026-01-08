package com.nextepisode.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieStatus {
    private Boolean isFavorite = false;
    private Boolean watched = false;
    private Boolean inWatchlist = false;

    // Constructor for creating default "not found" status
    public static MovieStatus defaultStatus() {
        return new MovieStatus(false, false, false);
    }
}
