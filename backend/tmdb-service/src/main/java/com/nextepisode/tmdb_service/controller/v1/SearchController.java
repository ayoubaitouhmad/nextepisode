package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.service.SearchService;
import com.nextepisode.tmdb_service.tmdb.response.LanguageList;
import com.nextepisode.tmdb_service.tmdb.response.PersonList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(ApiPaths.API_V1 + "/search")
@RestController
public class SearchController {
    private final SearchService searchService;
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/person")
    public PersonList searchPerson(
            @RequestParam("query") String query,
            @RequestParam("page") Integer page
    ) {
        return searchService.searchPerson(query, page);
    }
}
