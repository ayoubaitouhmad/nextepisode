package com.nextepisode.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data  // ✅ Add this!
@NoArgsConstructor
@AllArgsConstructor
public class MovieStatusList {
    /***
     * Long: movie id
     * MovieStatus: movie status
     */
    private Map<Long, MovieStatus> moviesStatus;
}
