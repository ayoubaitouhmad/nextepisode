package com.nextepisode.tmdb_service.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class CacheConfig {

    public final static String TMDB_API_LANGUAGES = "tmdb_api_languages";
    public final static String TMDB_API_COUNTRIES = "countries";
    public final static String TMDB_API_MOVIE_GENRES = "movieGenres";
    public final static String TMDB_API_TVSHOW_GENRES = "tvShowGenres";
    public final static String YEARS = "years";


    @Bean
    public Caffeine<Object, Object> caffeineSpec() {
        return Caffeine.newBuilder()
                .maximumSize(10_000)
                .recordStats();
    }

    @Bean
    public CacheManager caffeineCacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cm = new CaffeineCacheManager(
                cacheNames()
        );
        cm.setCaffeine(caffeine);
        return cm; // Start with Caffeine only
    }

    private String[] cacheNames() {
        return new String[]{
                TMDB_API_LANGUAGES,
                TMDB_API_COUNTRIES,
                TMDB_API_MOVIE_GENRES,
                TMDB_API_TVSHOW_GENRES,
                YEARS
        };
    }
}
