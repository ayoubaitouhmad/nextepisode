package com.nextepisode.tmdb_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public abstract class BaseService {
    protected  RestClient TMDBClient;
    @Autowired
    public BaseService(RestClient TMDBClient) {
        this.TMDBClient = TMDBClient;
    }
}
