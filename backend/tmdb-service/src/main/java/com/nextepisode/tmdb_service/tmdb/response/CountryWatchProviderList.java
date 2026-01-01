package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.common.WatchProvider;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CountryWatchProviderList extends Watching {

    @JsonProperty("buy")
    private List<WatchProvider> buy;
    @JsonProperty("rent")
    private List<WatchProvider> rent;

    public boolean hasProviders() {
        return buy != null || rent != null;
    }
}
