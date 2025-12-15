package com.nextepisode.tmdb_service.tmdb.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@SuperBuilder
public abstract class StringElement extends AbstractJsonMapping {
    @JsonProperty("id")
    private String id;
}
