package com.nextepisode.auth_service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Standardized error response DTO sent to clients.
 * Provides rich error information for better debugging and UX.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Unique error code for programmatic handling (e.g., "AUTH_001")
     */
    private String code;

    /**
     * Human-readable error message
     */
    private String message;

    /**
     * HTTP status code
     */
    private int status;

    /**
     * Error type for client-side categorization
     */
    private String type;

    /**
     * Request path that caused the error
     */
    private String path;

    /**
     * Timestamp when error occurred
     */
    private Instant timestamp;

    /**
     * Optional field validation errors
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> fieldErrors;

    /**
     * Optional trace ID for server logging correlation
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String traceId;
}