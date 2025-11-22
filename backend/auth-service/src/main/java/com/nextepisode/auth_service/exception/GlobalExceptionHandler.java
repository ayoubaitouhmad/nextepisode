package com.nextepisode.auth_service.exception;

import com.nextepisode.auth_service.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Centralized exception handling for all REST endpoints.
 * <p>
 * Design principles:
 * - Grouped handlers by exception category
 * - No magic numbers or hardcoded strings
 * - Consistent error response format
 * - Easy to extend for new exception types
 * - Proper logging for debugging
 * <p>
 * Handler organization:
 * 1. Application exceptions (your business logic)
 * 2. Validation exceptions (all validation types grouped)
 * 3. Database exceptions (integrity violations)
 * 4. Fallback handler (unexpected errors)
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================================
    // HANDLER 1: Application Business Exceptions
    // ============================================

    /**
     * Handles all application-specific exceptions.
     * These are YOUR exceptions that contain pre-defined error codes and HTTP statuses.
     * <p>
     * Examples:
     * - AuthenticationException (login, auth failures)
     * - ValidationException (custom validation rules)
     * - ResourceNotFoundException
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
                ex.getMessageArgs()  // Pass message args to format the template
        );

        log.warn("Application exception occurred: code={}, message={}",
                ex.getErrorName(), ex.getMessage());

        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(errorResponse);
    }

    // ============================================
    // HANDLER 2: Custom Validation Exceptions
    // ============================================

    /**
     * Handles custom validation exceptions from service layer.
     * Can include field-level error details.
     * <p>
     * Example:
     * - throw new ValidationException(ErrorCode.INVALID_INPUT, "field", "message");
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            ValidationException ex,
            WebRequest request) {

        ErrorResponse errorResponse = buildErrorResponseWithFieldErrors(
                ex.getErrorCode(),
                ex.getErrorCode().getHttpStatus(),
                getRequestPath(request),
                null,
                ex.getFieldErrors(),
                ex.getMessageArgs()
        );

        log.warn("Validation exception occurred: code={}, message={}",
                ex.getErrorName(), ex.getMessage());

        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(errorResponse);
    }

    // ============================================
    // HANDLER 3: Framework Validation Exceptions
    // ============================================

    /**
     * Handles Jakarta Bean Validation constraint violations.
     * These are thrown when @Email, @NotBlank, @Size, etc. fail on @RequestParam or @RequestBody.
     * <p>
     * Examples:
     * - @Email validation fails
     * - @NotBlank validation fails
     * - @Size validation fails
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {

        // Extract field errors from constraint violations
        Map<String, String> fieldErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            fieldErrors.put(fieldName, errorMessage);
        }

        ErrorResponse errorResponse = buildErrorResponseWithFieldErrors(
                ErrorCode.VALIDATION_FAILED,
                HttpStatus.BAD_REQUEST,
                getRequestPath(request),
                null,
                fieldErrors
        );

        log.warn("Constraint validation error occurred: {}", fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * Handles validation errors on @Valid @RequestBody.
     * Thrown when @RequestBody validation fails (e.g., @Valid on DTO fails).
     * <p>
     * Examples:
     * - Missing required field in DTO
     * - Invalid field value in DTO
     * - @Email, @NotBlank, etc. fail on DTO fields
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();

        // Extract field errors from binding result
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = buildErrorResponseWithFieldErrors(
                ErrorCode.VALIDATION_FAILED,
                HttpStatus.BAD_REQUEST,
                getRequestPath(request),
                null,
                fieldErrors
        );

        log.warn("Request body validation failed: {}", fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * Handles missing or invalid @RequestParam values.
     * Thrown when a required @RequestParam is missing.
     * <p>
     * Examples:
     * - Missing required parameter: @RequestParam(required = true) String email
     * - Parameter type mismatch
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameter(
            MissingServletRequestParameterException ex,
            WebRequest request) {

        String paramName = ex.getParameterName();
        String paramType = ex.getParameterType();

        Map<String, String> fieldErrors = new HashMap<>();
        fieldErrors.put(paramName, String.format(
                "Required %s parameter '%s' is not present",
                paramType,
                paramName
        ));

        ErrorResponse errorResponse = buildErrorResponseWithFieldErrors(
                ErrorCode.FIELD_REQUIRED,
                HttpStatus.BAD_REQUEST,
                getRequestPath(request),
                null,
                fieldErrors,
                paramName
        );

        log.warn("Missing request parameter: parameter={}, type={}", paramName, paramType);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    // ============================================
    // HANDLER 4: Database Exceptions
    // ============================================

    /**
     * Handles database integrity violations (duplicate entries, constraints).
     * Maps database errors to appropriate application error codes.
     * <p>
     * Examples:
     * - Duplicate email
     * - Duplicate username
     * - Constraint violations
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {

        // Determine if it's a duplicate key error or other constraint
        ErrorCode errorCode = determineDatabaseErrorCode(ex);

        ErrorResponse errorResponse = buildErrorResponse(
                errorCode,
                errorCode.getHttpStatus(),
                getRequestPath(request),
                null
        );

        log.error("Database integrity violation: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    // ============================================
    // HANDLER 5: Fallback Handler
    // ============================================

    /**
     * Fallback handler for unexpected runtime exceptions.
     * This catches ANY exception not handled by the above handlers.
     * <p>
     * Examples:
     * - NullPointerException
     * - RuntimeException from third-party libraries
     * - Any unexpected error
     * <p>
     * This handler generates a trace ID to help debugging.
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

        log.error("Unexpected runtime exception [traceId={}]: {} | Message: {}",
                traceId, ex.getClass().getSimpleName(), ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Builds error response with just message formatting (no field errors).
     *
     * @param errorCode   The error code
     * @param status      HTTP status
     * @param path        Request path
     * @param traceId     Optional trace ID for debugging
     * @param messageArgs Optional arguments to format the message template
     * @return Constructed ErrorResponse
     */
    private ErrorResponse buildErrorResponse(
            ErrorCode errorCode,
            HttpStatus status,
            String path,
            String traceId,
            Object... messageArgs) {

        String formattedMessage = (messageArgs != null && messageArgs.length > 0)
                ? errorCode.getMessage(messageArgs)
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
     * Builds error response with field-level validation errors.
     *
     * @param errorCode   The error code
     * @param status      HTTP status
     * @param path        Request path
     * @param traceId     Optional trace ID for debugging
     * @param fieldErrors Map of field names to error messages
     * @return Constructed ErrorResponse with fieldErrors
     */
    private ErrorResponse buildErrorResponseWithFieldErrors(
            ErrorCode errorCode,
            HttpStatus status,
            String path,
            String traceId,
            Map<String, String> fieldErrors) {

        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessageTemplate())
                .status(status.value())
                .type(categorizeErrorType(status))
                .path(path)
                .timestamp(Instant.now())
                .traceId(traceId)
                .fieldErrors(fieldErrors)
                .build();
    }

    /**
     * Builds error response with both field errors and message parameter formatting.
     *
     * @param errorCode   The error code
     * @param status      HTTP status
     * @param path        Request path
     * @param traceId     Optional trace ID for debugging
     * @param fieldErrors Map of field names to error messages
     * @param messageArgs Optional arguments to format the message template
     * @return Constructed ErrorResponse with fieldErrors and formatted message
     */
    private ErrorResponse buildErrorResponseWithFieldErrors(
            ErrorCode errorCode,
            HttpStatus status,
            String path,
            String traceId,
            Map<String, String> fieldErrors,
            Object... messageArgs) {

        String formattedMessage = (messageArgs != null && messageArgs.length > 0)
                ? errorCode.getMessage(messageArgs)
                : errorCode.getMessageTemplate();

        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(formattedMessage)
                .status(status.value())
                .type(categorizeErrorType(status))
                .path(path)
                .timestamp(Instant.now())
                .traceId(traceId)
                .fieldErrors(fieldErrors)
                .build();
    }

    /**
     * Determines the appropriate ErrorCode from a database exception.
     * Maps database-level errors to business-level error codes.
     * <p>
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
     *
     * @param status HTTP status code
     * @return Error type: "CLIENT_ERROR" (4xx), "SERVER_ERROR" (5xx), or "UNKNOWN"
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
     * Falls back to "unknown" if extraction fails.
     *
     * @param request The web request
     * @return Request path (e.g., "/api/auth/register")
     */
    private String getRequestPath(WebRequest request) {
        try {
            return request.getDescription(false).replace("uri=", "");
        } catch (Exception e) {
            log.warn("Could not extract request path from request", e);
            return "unknown";
        }
    }
}