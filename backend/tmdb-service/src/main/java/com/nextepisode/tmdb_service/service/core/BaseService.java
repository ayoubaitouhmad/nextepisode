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
}
