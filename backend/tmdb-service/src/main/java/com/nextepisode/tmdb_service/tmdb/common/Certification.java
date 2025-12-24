package com.nextepisode.tmdb_service.tmdb.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certification {

    @JsonProperty("certification")
    private String certification;

    @JsonProperty("meaning")
    private String meaning;

    @JsonProperty("order")
    private Integer order;
}