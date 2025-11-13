package com.nextepisode.tmdb_service.dto.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TMDBCountry {
    @JsonProperty("iso_3166_1")
    public String ISO;
    @JsonProperty("english_name")
    public String englishName;
    @JsonProperty("native_name")
    public String nativeName;

}
