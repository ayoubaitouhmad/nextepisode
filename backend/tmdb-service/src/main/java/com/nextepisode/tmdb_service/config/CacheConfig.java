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

    @Bean
    public Caffeine<Object, Object> caffeineSpec() {
        return Caffeine.newBuilder()
                .maximumSize(10_000)
                .recordStats();
    }

    @Bean
    public CacheManager caffeineCacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cm = new CaffeineCacheManager(
                "movieGenres",
                "countries"

        );
        cm.setCaffeine(caffeine);
        return cm; // Start with Caffeine only
    }

    // Later, if you want Redis too:
    // @Bean
    // public RedisCacheManager redisCacheManager(RedisConnectionFactory factory) {
    //   RedisCacheConfiguration cfg = RedisCacheConfiguration.defaultCacheConfig()
    //     .entryTtl(Duration.ofHours(12))
    //     .disableCachingNullValues();
    //   return RedisCacheManager.builder(factory).cacheDefaults(cfg).build();
    // }

    // @Primary
    // @Bean
    // public CompositeCacheManager composite(CacheManager caffeine, RedisCacheManager redis) {
    //   CompositeCacheManager cm = new CompositeCacheManager(caffeine, redis);
    //   cm.setFallbackToNoOpCache(false);
    //   return cm;
    // }
}
