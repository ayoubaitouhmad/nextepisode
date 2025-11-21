package com.nextepisode.auth_service.exception;

import com.nextepisode.auth_service.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.UUID;

/**
 * Centralized exception handling for all REST endpoints.
 * <p>
 * Design principles:
 * - Single handler per exception type
 * - No magic numbers or hardcoded strings
 * - Consistent error response format
 * - Easy to extend for new exception types
 * - Proper logging for debugging
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles all application-specific exceptions.
     * These contain pre-defined error codes and HTTP statuses.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException ex,
            WebRequest request) {

        ErrorResponse errorResponse = buildErrorResponse(
                ex.getErrorCode(),
                ex.getErrorCode().getHttpStatus(),
                getRequestPath(request),
                null,
                ex.getMessageArgs()  // ✅ NEW: Pass the args
        );
        log.warn("Application exception occurred: code={}, message={}",
                ex.getErrorName(), ex.getMessage());

        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(errorResponse);
    }

    /**
     * Handles database integrity violations (duplicate entries, constraints).
     * Maps database errors to appropriate application error codes.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {

        // Determine if it's a duplicate key error
        ErrorCode errorCode = determineDatabaseErrorCode(ex);

        ErrorResponse errorResponse = buildErrorResponse(
                errorCode,
                errorCode.getHttpStatus(),
                getRequestPath(request),
                null,
                ex.getMessage()
        );

        log.error("Database integrity violation: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * Fallback handler for unexpected runtime exceptions.
     * Should rarely occur if all expected exceptions are handled above.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex,
            WebRequest request) {

        String traceId = UUID.randomUUID().toString();

        ErrorResponse errorResponse = buildErrorResponse(
                ErrorCode.INTERNAL_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                getRequestPath(request),
                traceId
        );

        log.error("Unexpected runtime exception [traceId={}]: {}",
                traceId, ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    /**
     * Helper method to build consistent error responses.
     * Reduces boilerplate and ensures all errors follow the same structure.
     */
    private ErrorResponse buildErrorResponse(
            ErrorCode errorCode,
            HttpStatus status,
            String path,
            String traceId,
            Object... messageArgs

    ) {

        String formattedMessage = (messageArgs != null && messageArgs.length > 0)
                ? errorCode.getMessage(messageArgs)  // ✅ Format with args
                : errorCode.getMessageTemplate();

        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(formattedMessage)
                .status(status.value())
                .type(categorizeErrorType(status))
                .path(path)
                .timestamp(Instant.now())
                .traceId(traceId)
                .build();
    }

    /**
     * Determines the appropriate ErrorCode from a database exception.
     * Can be extended to handle more specific database error cases.
     */
    private ErrorCode determineDatabaseErrorCode(DataIntegrityViolationException ex) {
        String message = ex.getMessage().toLowerCase();

        if (message.contains("username")) {
            return ErrorCode.USERNAME_ALREADY_EXISTS;
        } else if (message.contains("email")) {
            return ErrorCode.EMAIL_ALREADY_EXISTS;
        }

        return ErrorCode.DATABASE_ERROR;
    }

    /**
     * Categorizes errors by type for client-side handling.
     * Allows clients to handle different error categories differently.
     */
    private String categorizeErrorType(HttpStatus status) {
        if (status.is4xxClientError()) {
            return "CLIENT_ERROR";
        } else if (status.is5xxServerError()) {
            return "SERVER_ERROR";
        }
        return "UNKNOWN";
    }

    /**
     * Safely extracts request path from WebRequest.
     */
    private String getRequestPath(WebRequest request) {
        try {
            return request.getDescription(false).replace("uri=", "");
        } catch (Exception e) {
            return "unknown";
        }
    }
}