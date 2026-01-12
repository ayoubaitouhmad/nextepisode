package com.nextepisode.user_service.dto.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieIds {
    private List<Integer> movieIds;

}
