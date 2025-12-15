package com.nextepisode.tmdb_service.tmdb.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.nextepisode.tmdb_service.tmdb.core.IdElement;
import com.nextepisode.tmdb_service.tmdb.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class Person extends IdElement {
    @JsonProperty("Adult")
    private Boolean Adult;

    @JsonProperty("gender")
    private Gender gender;

    @JsonProperty("name")
    private String name;

    @JsonProperty("original_name")
    private String originalName;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    @JsonProperty("popularity")
    private Double popularity;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("known_for")
    private List<Object> knownFor;
}
