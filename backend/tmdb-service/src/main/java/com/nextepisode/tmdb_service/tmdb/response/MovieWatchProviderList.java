package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Data
@Builder
public class MovieWatchProviderList extends Watching {

    @JsonProperty("results")
    public Map<String, CountryWatchProviderList> providers;

    public CountryWatchProviderList getWatchProviderByRegion(String region) {
        if (providers == null) {
            return null;
        }
        return providers.get(region);
    }



}
