package com.nextepisode.tmdb_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SearchService  extends BaseService{
    public SearchService(RestClient TMDBClient) {
        super(TMDBClient);
    }
}
