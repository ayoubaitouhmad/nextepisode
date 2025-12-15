package com.nextepisode.tmdb_service.tmdb.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TMDBLanguage {

    @JsonProperty("iso_639_1")
    public String isoNationalCode;
    @JsonProperty("english_name")
    public String englishName;
    @JsonProperty("name")
    public String name;


}
