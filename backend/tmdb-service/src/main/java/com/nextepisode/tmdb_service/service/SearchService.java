package com.nextepisode.tmdb_service.service;

import com.nextepisode.tmdb_service.tmdb.response.PersonList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class SearchService  extends BaseService{
    public SearchService(RestClient tmdbClient) {
        super(tmdbClient);
    }
    public PersonList searchPerson(String query , Integer page) {
        log.info("searchPerson");
        try {
            return  tmdbClient.get().uri(uriBuilder -> uriBuilder
                            .path("/search/person")
                            .queryParam("query", query)
                            .queryParam("language", "en-US")
                            .queryParam("page", page)
                            .build()
                    )
                    .retrieve()
                    .body(PersonList.class);

        }catch (Exception e){
            log.error("Error while getting person by query:{}",query,e);
            throw e;
        }
    }
}
