package com.nextepisode.tmdb_service.controller.v1;

import com.nextepisode.tmdb_service.config.ApiPaths;
import com.nextepisode.tmdb_service.service.CertificationService;
import com.nextepisode.tmdb_service.tmdb.common.Certification;
import com.nextepisode.tmdb_service.tmdb.response.CertificationList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping(ApiPaths.API_V1 + "/certifications")
@RestController
public class CertificationController {

    private final CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping("/movies")
    public ResponseEntity<CertificationList> getMovieCertifications() {
        return ResponseEntity.ok(certificationService.getMoviesCertifications());
    }

    @GetMapping("/movies/{countryCode}")
    public ResponseEntity<List<Certification>> getMovieCertificationsByCountry(
            @PathVariable String countryCode) {
        List<Certification> certifications =
                certificationService.getMoviesCertificationsByCountry(countryCode.toUpperCase());

        if (certifications == null || certifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(certifications);
    }


    @GetMapping("/tv")
    public ResponseEntity<CertificationList> getTvCertifications() {
        return ResponseEntity.ok(certificationService.getTvShowsCertifications());
    }

    @GetMapping("/tv/{countryCode}")
    public ResponseEntity<List<Certification>> getTvShowsCertificationsByCountry(
            @PathVariable String countryCode) {
        List<Certification> certifications =
                certificationService.getTvShowsCertificationsByCountry(countryCode.toUpperCase());

        if (certifications == null || certifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(certifications);
    }


}
