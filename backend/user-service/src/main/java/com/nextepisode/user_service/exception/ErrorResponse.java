package com.nextepisode.user_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized error response format.
 * This provides a consistent structure for all error responses.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp when the error occurred
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code
     */
    private int status;

    /**
     * Error type/category
     */
    private String error;

    /**
     * Human-readable error message
     */
    private String message;

    /**
     * Additional detail about the error
     */
    private String detail;

    /**
     * The request path that caused the error
     */
    private String path;

    /**
     * Field-level validation errors (for validation failures)
     */
    private Map<String, String> validationErrors;

    /**
     * Unique error code for tracking (optional)
     */
    private String errorCode;

    /**
     * Stack trace (only in development mode)
     */
    private String debugMessage;
}