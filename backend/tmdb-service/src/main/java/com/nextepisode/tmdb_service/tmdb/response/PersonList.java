package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.common.Person;

import java.util.List;

public class PersonList {
    @JsonProperty("page")
    public Integer page;
    @JsonProperty("total_pages")
    public Integer totalPages;
    @JsonProperty("total_results")
    public Integer totalResults;

    @JsonProperty("results")
    public List<Person> persons;
}
