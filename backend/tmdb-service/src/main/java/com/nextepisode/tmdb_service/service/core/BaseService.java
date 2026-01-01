package com.nextepisode.tmdb_service.service.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
@Slf4j
public abstract class BaseService {
    protected final RestClient tmdbClient;

    /***
     * Generic get request for the Tmdb Rest Client
     * @param uriFunction URI builder lambda
     * @param responseType response class
     * @return
     * @param <T>
     */
    protected  <T> T get(
            Function<UriBuilder, URI> uriFunction,
            Class<T> responseType
    ) {
        log.debug("TMDB GET request for responseType={} with path:{}", responseType.getSimpleName() , uriFunction.toString());
        return tmdbClient.get()
                .uri(uriFunction)
                .retrieve()
                .body(responseType);
    }
}
