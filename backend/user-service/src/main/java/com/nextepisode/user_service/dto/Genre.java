package com.nextepisode.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Genre {

    @JsonProperty("id")
    private Number id;
    @JsonProperty("name")
    private String name;

}
