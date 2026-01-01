package com.nextepisode.tmdb_service.service;

import com.nextepisode.tmdb_service.config.CacheConfig;
import com.nextepisode.tmdb_service.service.core.BaseService;
import com.nextepisode.tmdb_service.tmdb.common.Certification;
import com.nextepisode.tmdb_service.tmdb.response.CertificationList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class CertificationService extends BaseService {

    public CertificationService(RestClient tmdbClient) {
        super(tmdbClient);
    }

    @Cacheable(CacheConfig.MOVIE_CERTIFICATIONS)
    public CertificationList getMoviesCertifications() {
        log.info("Start getting movie certfications");
        try {
            return fetchCertifications("/certification/movie/list");
        } catch (Exception e) {
            log.error("Failed getting tmdb certfications: ", e);
            throw new RuntimeException("Failed getting tmdb certfications: ", e);
        }
    }

    @Cacheable(CacheConfig.TVSHOWS_CERTIFICATIONS)
    public CertificationList getTvShowsCertifications() {
        log.info("Start getting tv show certfications");
        try {
            return fetchCertifications("/certification/tv/list");
        } catch (Exception e) {
            log.error("Failed getting tv show certfications: ", e);
            throw new RuntimeException("Failed getting tv show certfications: ", e);
        }
    }

    public CertificationList fetchCertifications(String path) {
        return tmdbClient.get().uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("language", "en-US")
                        .build())
                .retrieve()
                .body(CertificationList.class);
    }

    @Cacheable(CacheConfig.MOVIE_CERTIFICATIONS)
    public List<Certification> getMoviesCertificationsByCountry(String country) {
        CertificationList getTvShowsCertifications = getMoviesCertifications();
        return getTvShowsCertifications.getCertificationsByCountry(country);
    }
    @Cacheable(CacheConfig.TVSHOWS_CERTIFICATIONS)
    public List<Certification> getTvShowsCertificationsByCountry(String country) {
        CertificationList getTvShowsCertifications = getTvShowsCertifications();
        return getTvShowsCertifications.getCertificationsByCountry(country);
    }
}
