package com.nextepisode.tmdb_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Centralized error code enumeration. All possible errors are defined here.
 * Adding new errors is as simple as adding an enum constant.
 * This promotes discoverability and makes error handling explicit.
 */
public enum ErrorCode {

    // Authentication errors (4xx)


    // ============= GENERAL ERRORS (1000-1099) =============
    INTERNAL_SERVER_ERROR("TMDB-1000", "Internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST("TMDB-1001", "Invalid request parameters", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND("TMDB-1002", "Requested resource not found", HttpStatus.NOT_FOUND),
    VALIDATION_ERROR("TMDB-1003", "Validation error", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("TMDB-1004", "Unauthorized access", HttpStatus.UNAUTHORIZED),


    // ============= VALIDATION ERRORS (2000-2099) =============
    VALIDATION_FAILED(
            "VAL_001",
            "Input validation failed",
            HttpStatus.BAD_REQUEST
    ),
    FIELD_REQUIRED(
            "VAL_002",
            "Required field is missing: {0}",
            HttpStatus.BAD_REQUEST
    ),
    // ============= TMDB API ERRORS (2000-2099) =============
    // ============= MOVIE ERRORS (3000-3099) =============
    // ============= TV SHOW ERRORS (4000-4099) =============
    // ============= GENRE ERRORS (5000-5099) =============
    // ============= DISCOVER/FILTER ERRORS (6000-6099) =============
    INVALID_DISCOVER_PARAMS("TMDB-6000",
            "Invalid discover parameters",
            HttpStatus.BAD_REQUEST),
    DISCOVER_FAILED("TMDB-6001",
            "Movie discovery failed",
            HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_SORT_OPTION("TMDB-6002",
            "Invalid sort option provided",
            HttpStatus.BAD_REQUEST),
    INVALID_FILTER_OPTION("TMDB-6003",
            "Invalid filter option provided",
            HttpStatus.BAD_REQUEST),
    INVALID_PAGE_NUMBER("TMDB-6004",
            "Invalid page number. Must be between 1 and 500",
            HttpStatus.BAD_REQUEST),
    INVALID_LANGUAGE_CODE("TMDB-6005",
            "Invalid language code. Must be in ISO 639-1 format (e.g., 'en', 'fr')",
            HttpStatus.BAD_REQUEST),
    INVALID_REGION_CODE("TMDB-6006",
            "Invalid region code. Must be in ISO 3166-1 format (e.g., 'US', 'FR')",
            HttpStatus.BAD_REQUEST),
    INVALID_YEAR("TMDB-6007",
            "Invalid year provided",
            HttpStatus.BAD_REQUEST),
    INVALID_YEAR_RANGE("TMDB-6008",
            "Invalid year range. 'yearFrom' must be less than or equal to 'yearTo'",
            HttpStatus.BAD_REQUEST),
    INVALID_GENRE_LIST("TMDB-6009",
            "Invalid genre list. Genre IDs must be positive integers",
            HttpStatus.BAD_REQUEST),
    INVALID_WATCH_PROVIDER_LIST("TMDB-6010",
            "Invalid watch provider list. Provider IDs must be positive integers",
            HttpStatus.BAD_REQUEST),
    INVALID_CERTIFICATION("TMDB-6011",
            "Invalid certification value",
            HttpStatus.BAD_REQUEST),
    YEAR_OUT_OF_RANGE("TMDB-6012",
            "Year must be between 1900 and current year + 5",
            HttpStatus.BAD_REQUEST),


    ;


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
     * Formats the message template with provided arguments.
     * Supports parameterized messages like "User not found: {0}"
     */
    public String getMessage(Object... args) {
        if (args.length == 0) {
            return messageTemplate;
        }
        String message = messageTemplate;
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return message;
    }
}