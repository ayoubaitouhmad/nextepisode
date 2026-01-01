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
    public final static String MOVIE_CERTIFICATIONS = "movies.certifications";
    public final static String TVSHOWS_CERTIFICATIONS = "tvshows.certifications";
    public final static String MOVIE_MOVIE_DETAIL = "movie.movie_details";
    public final static String MOVIE_TOP_RATED = "movie.top_rated";
    public final static String MOVIE_POPULAR = "movie.polular";
    public final static String MOVIE_WATCH_PROVIDER = "movie.watch_provider";


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
                YEARS,
                MOVIE_CERTIFICATIONS,
                TVSHOWS_CERTIFICATIONS,
                MOVIE_MOVIE_DETAIL,
                MOVIE_TOP_RATED,
                MOVIE_POPULAR,
                MOVIE_WATCH_PROVIDER
        };
    }
}
