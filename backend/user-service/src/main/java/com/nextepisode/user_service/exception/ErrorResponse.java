package com.nextepisode.user_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

/**
 * Standardized API error response format.
 * <p>
 * This ensures all errors returned by the API have a consistent structure,
 * making it easier for clients to parse and handle errors uniformly.
 * <p>
 * Fields are only included in JSON if non-null (via JsonInclude.NON_NULL),
 * keeping responses clean and only showing relevant information.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * ISO-8601 timestamp when the error occurred.
     * Using Instant for timezone-agnostic, sortable timestamps.
     */
    private final Instant timestamp;

    /**
     * HTTP status code (e.g., 400, 404, 500)
     */
    private final int status;

    /**
     * Error category for quick identification.
     * Examples: "VALIDATION_ERROR", "NOT_FOUND", "INTERNAL_ERROR"
     */
    private final String type;

    /**
     * Machine-readable error code for client-side handling.
     * Maps directly to ErrorCode enum values (e.g., "AUTH_001", "VAL_002")
     */
    private final String code;

    /**
     * Human-readable error message suitable for display.
     */
    private final String message;

    /**
     * Additional context about the error (optional).
     * Can contain more specific details without exposing internals.
     */
    private final String detail;

    /**
     * The API endpoint path that triggered the error.
     */
    private final String path;

    /**
     * Field-level validation errors.
     * Key: field name, Value: error message for that field.
     * Only populated for validation-type errors.
     */
    private final Map<String, String> fieldErrors;

    /**
     * Unique trace ID for debugging and log correlation.
     * Useful for tracking errors across distributed systems.
     */
    private final String traceId;

    /**
     * Factory method for creating a simple error response.
     */
    public static ErrorResponse of(int status, String code, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status)
                .code(code)
                .message(message)
                .path(path)
                .build();
    }
}