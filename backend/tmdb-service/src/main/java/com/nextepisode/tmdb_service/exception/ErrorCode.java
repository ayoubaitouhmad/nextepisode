package com.nextepisode.tmdb_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Centralized error code enumeration for NextEpisode application.
 *
 * Error Code Structure: [DOMAIN]-[RANGE][NUMBER]
 * - GEN: General errors (1000-1999)
 * - API: External API errors (2000-2999)
 * - MOV: Movie-specific errors (3000-3999)
 * - TV:  TV Show-specific errors (4000-4999)
 * - GNR: Genre-related errors (5000-5999)
 * - DSC: Discovery/Filtering errors (6000-6999)
 * - REG: Region-related errors (7000-7999)
 * - VAL: Validation errors (8000-8999)
 *
 * Each domain has 1000 slots, allowing easy expansion while maintaining organization.
 */
public enum ErrorCode {

    // ============================================================================
    // GENERAL ERRORS (GEN: 1000-1999)
    // ============================================================================

    GENERAL_INTERNAL_ERROR(
            "GEN-1000",
            "An internal server error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),

    GENERAL_INVALID_REQUEST(
            "GEN-1001",
            "Invalid request parameters provided",
            HttpStatus.BAD_REQUEST
    ),

    GENERAL_RESOURCE_NOT_FOUND(
            "GEN-1002",
            "The requested resource was not found",
            HttpStatus.NOT_FOUND
    ),

    GENERAL_UNAUTHORIZED(
            "GEN-1003",
            "Unauthorized access attempt",
            HttpStatus.UNAUTHORIZED
    ),

    GENERAL_FORBIDDEN(
            "GEN-1004",
            "Access to this resource is forbidden",
            HttpStatus.FORBIDDEN
    ),

    GENERAL_SERVICE_UNAVAILABLE(
            "GEN-1005",
            "Service is temporarily unavailable",
            HttpStatus.SERVICE_UNAVAILABLE
    ),

    // ============================================================================
    // EXTERNAL API ERRORS (API: 2000-2999)
    // ============================================================================

    API_COMMUNICATION_ERROR(
            "API-2000",
            "Failed to communicate with external movie database",
            HttpStatus.BAD_GATEWAY
    ),

    API_RATE_LIMIT_EXCEEDED(
            "API-2001",
            "External API rate limit exceeded. Please try again later",
            HttpStatus.TOO_MANY_REQUESTS
    ),

    API_TIMEOUT(
            "API-2002",
            "External API request timed out",
            HttpStatus.GATEWAY_TIMEOUT
    ),

    API_INVALID_RESPONSE(
            "API-2003",
            "Received invalid response from external API",
            HttpStatus.BAD_GATEWAY
    ),

    API_SERVICE_UNAVAILABLE(
            "API-2004",
            "External movie database is currently unavailable",
            HttpStatus.SERVICE_UNAVAILABLE
    ),

    // ============================================================================
    // MOVIE ERRORS (MOV: 3000-3999)
    // ============================================================================

    MOVIE_NOT_FOUND(
            "MOV-3000",
            "Movie not found with ID: {0}",
            HttpStatus.NOT_FOUND
    ),

    MOVIE_NOT_AVAILABLE_IN_REGION(
            "MOV-3001",
            "Movie is not available in region: {0}",
            HttpStatus.NOT_FOUND
    ),

    MOVIE_ADULT_CONTENT_RESTRICTED(
            "MOV-3002",
            "Access restricted: Movie contains adult content",
            HttpStatus.FORBIDDEN
    ),

    MOVIE_WATCH_PROVIDERS_NOT_FOUND(
            "MOV-3003",
            "No streaming providers found for movie ID: {0}",
            HttpStatus.NOT_FOUND
    ),

    MOVIE_DETAILS_UNAVAILABLE(
            "MOV-3004",
            "Movie details are currently unavailable",
            HttpStatus.SERVICE_UNAVAILABLE
    ),

    // ============================================================================
    // TV SHOW ERRORS (TV: 4000-4999)
    // ============================================================================

    TV_SHOW_NOT_FOUND(
            "TV-4000",
            "TV show not found with ID: {0}",
            HttpStatus.NOT_FOUND
    ),

    TV_SHOW_NOT_AVAILABLE_IN_REGION(
            "TV-4001",
            "TV show is not available in region: {0}",
            HttpStatus.NOT_FOUND
    ),

    TV_SHOW_ADULT_CONTENT_RESTRICTED(
            "TV-4002",
            "Access restricted: TV show contains adult content",
            HttpStatus.FORBIDDEN
    ),

    // ============================================================================
    // GENRE ERRORS (GNR: 5000-5999)
    // ============================================================================

    GENRE_NOT_FOUND(
            "GNR-5000",
            "Genre not found with ID: {0}",
            HttpStatus.NOT_FOUND
    ),

    GENRE_LIST_UNAVAILABLE(
            "GNR-5001",
            "Genre list is currently unavailable",
            HttpStatus.SERVICE_UNAVAILABLE
    ),

    // ============================================================================
    // DISCOVERY & FILTERING ERRORS (DSC: 6000-6999)
    // ============================================================================

    DISCOVER_INVALID_PARAMETERS(
            "DSC-6000",
            "Invalid discovery parameters provided",
            HttpStatus.BAD_REQUEST
    ),

    DISCOVER_OPERATION_FAILED(
            "DSC-6001",
            "Movie discovery operation failed",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),

    DISCOVER_INVALID_SORT_OPTION(
            "DSC-6002",
            "Invalid sort option: {0}",
            HttpStatus.BAD_REQUEST
    ),

    DISCOVER_INVALID_FILTER_OPTION(
            "DSC-6003",
            "Invalid filter option: {0}",
            HttpStatus.BAD_REQUEST
    ),

    DISCOVER_INVALID_PAGE_NUMBER(
            "DSC-6004",
            "Invalid page number. Must be between 1 and 500",
            HttpStatus.BAD_REQUEST
    ),

    DISCOVER_INVALID_YEAR(
            "DSC-6005",
            "Invalid year: {0}",
            HttpStatus.BAD_REQUEST
    ),

    DISCOVER_INVALID_YEAR_RANGE(
            "DSC-6006",
            "Invalid year range. Start year must be less than or equal to end year",
            HttpStatus.BAD_REQUEST
    ),

    DISCOVER_YEAR_OUT_OF_RANGE(
            "DSC-6007",
            "Year must be between 1900 and {0}",
            HttpStatus.BAD_REQUEST
    ),

    DISCOVER_INVALID_CERTIFICATION(
            "DSC-6008",
            "Invalid certification value: {0}",
            HttpStatus.BAD_REQUEST
    ),

    // ============================================================================
    // REGION ERRORS (REG: 7000-7999)
    // ============================================================================

    REGION_INVALID_CODE(
            "REG-7000",
            "Invalid region code: {0}. Must be in ISO 3166-1 format (e.g., 'US', 'FR')",
            HttpStatus.BAD_REQUEST
    ),

    REGION_NOT_SUPPORTED(
            "REG-7001",
            "Region not supported: {0}",
            HttpStatus.BAD_REQUEST
    ),

    REGION_LANGUAGE_INVALID(
            "REG-7002",
            "Invalid language code: {0}. Must be in ISO 639-1 format (e.g., 'en', 'fr')",
            HttpStatus.BAD_REQUEST
    ),

    // ============================================================================
    // VALIDATION ERRORS (VAL: 8000-8999)
    // ============================================================================

    VALIDATION_FAILED(
            "VAL-8000",
            "Input validation failed",
            HttpStatus.BAD_REQUEST
    ),

    VALIDATION_FIELD_REQUIRED(
            "VAL-8001",
            "Required field is missing: {0}",
            HttpStatus.BAD_REQUEST
    ),

    VALIDATION_FIELD_INVALID(
            "VAL-8002",
            "Invalid value for field: {0}",
            HttpStatus.BAD_REQUEST
    ),

    VALIDATION_GENRE_LIST_INVALID(
            "VAL-8003",
            "Invalid genre list. Genre IDs must be positive integers",
            HttpStatus.BAD_REQUEST
    ),

    VALIDATION_PROVIDER_LIST_INVALID(
            "VAL-8004",
            "Invalid watch provider list. Provider IDs must be positive integers",
            HttpStatus.BAD_REQUEST
    ),

    VALIDATION_ID_INVALID(
            "VAL-8005",
            "Invalid ID format. Must be a positive integer",
            HttpStatus.BAD_REQUEST
    ),

    VALIDATION_RATING_RANGE_INVALID(
            "VAL-8006",
            "Invalid rating range. Must be between 0 and 10",
            HttpStatus.BAD_REQUEST
    );

    // ============================================================================
    // ENUM FIELDS AND METHODS
    // ============================================================================

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String messageTemplate, HttpStatus httpStatus) {
        this.code = code;
        this.messageTemplate = messageTemplate;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * Formats the error message with provided arguments.
     * Supports parameterized messages using {0}, {1}, {2}, etc.
     *
     * @param args Arguments to replace placeholders in the message template
     * @return Formatted error message
     *
     * @example
     * ErrorCode.MOVIE_NOT_FOUND.getMessage(12345)
     * // Returns: "Movie not found with ID: 12345"
     */
    public String getMessage(Object... args) {
        if (args == null || args.length == 0) {
            return messageTemplate;
        }

        String message = messageTemplate;
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return message;
    }

    /**
     * Returns the domain category of this error code.
     *
     * @return The domain prefix (e.g., "GEN", "MOV", "API")
     */
    public String getDomain() {
        return code.substring(0, code.indexOf('-'));
    }

    /**
     * Checks if this error code belongs to a specific domain.
     *
     * @param domain The domain to check (e.g., "MOV", "API")
     * @return true if this error belongs to the specified domain
     */
    public boolean isDomain(String domain) {
        return getDomain().equals(domain);
    }
}