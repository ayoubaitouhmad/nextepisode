package com.nextepisode.tmdb_service.tmdb.core;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/***
 * This class (AbstractJsonMapping) is derived from ProjectName,c-eg/themoviedbapi
 * originally found at: https://github.com/c-eg/themoviedbapi/blob/master/src/main/java/info/movito/themoviedbapi/model/core/AbstractJsonMapping.java#L14
 * Original Author: [Holger Brandl]
 */
@NoArgsConstructor
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractJsonMapping implements Serializable {


    private final Map<String, Object> newItems = new HashMap<>();

    /**
     * Gets the new items that were not mapped to any field from a model extending this class.
     *
     * @return the new items.
     */
    @JsonAnyGetter
    public Map<String, Object> getNewItems() {
        return newItems;
    }

    @JsonAnySetter
    public void setNewItems(String name, Object value) {
        newItems.put(name, value);
    }
}