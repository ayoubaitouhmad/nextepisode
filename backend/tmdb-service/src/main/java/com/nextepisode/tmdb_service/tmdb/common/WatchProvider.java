package com.nextepisode.tmdb_service.tmdb.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.core.HasImagePaths;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class WatchProvider  implements HasImagePaths {

    @JsonProperty("provider_id")
    private Integer providerId;

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("logo_path")
    private String logoPath;

    @JsonProperty("display_priority")
    private Integer displayPriority;

    @JsonProperty("display_priorities")
    private Map<String, Integer> displayPriorities;

    public String getLogoPath() {
        return fullImageUrl(logoPath);
    }
}
