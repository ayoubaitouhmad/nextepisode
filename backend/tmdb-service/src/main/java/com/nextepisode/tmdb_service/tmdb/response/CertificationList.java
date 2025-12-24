package com.nextepisode.tmdb_service.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextepisode.tmdb_service.tmdb.common.Certification;
import com.nextepisode.tmdb_service.tmdb.common.Country;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CertificationList {


    @JsonProperty("certifications")
    private Map<String, List<Certification>> certifications;

    /**
     * Get certifications for a specific country
     * @param countryCode The ISO 3166-1 country code (e.g., "US", "DE", "FR")
     * @return List of certifications or null if not found
     */
    public List<Certification> getCertificationsByCountry(String countryCode) {
        return certifications != null ? certifications.get(countryCode) : null;
    }

    /**
     * Check if certifications exist for a country
     * @param countryCode The ISO 3166-1 country code
     * @return true if certifications exist
     */
    public boolean hasCertificationsForCountry(String countryCode) {
        return certifications != null && certifications.containsKey(countryCode);
    }
}
