package com.nextepisode.user_service.exception;

import com.nextepisode.user_service.exception.codes.*;
import com.nextepisode.user_service.exception.exceptions.ApplicationException;
import com.nextepisode.user_service.exception.exceptions.BusinessValidationException;
import com.nextepisode.user_service.exception.exceptions.ValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Global exception handler that provides consistent error responses across the API.
 * <p>
 * This class serves as a central place for converting exceptions into standardized
 * ErrorResponse objects. It handles:
 * <p>
 * 1. Custom application exceptions (our hierarchy extending ApplicationException)
 * 2. Spring validation exceptions (@Valid, @Validated)
 * 3. JWT/Security exceptions
 * 4. Database exceptions
 * 5. Unexpected exceptions (catch-all)
 * <p>
 * Each handler logs appropriately and returns a properly structured response,
 * making debugging easier while keeping client responses clean and consistent.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ========================================================================
    // APPLICATION EXCEPTION HANDLERS (Our Custom Exceptions)
    // ========================================================================

    /**
     * Handles all exceptions in our ApplicationException hierarchy.
     * This is the primary handler for business logic and validation errors.
     * <p>
     * Since all our custom exceptions extend ApplicationException and carry
     * an ErrorCode, we can handle them uniformly here.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException ex,
            WebRequest request) {

        // Generate trace ID for correlation in logs
        String traceId = generateTraceId();
        String path = extractPath(request);

        log.warn("Application exception [traceId={}]: code={}, message={}",
                traceId, ex.getCode(), ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(ex.getHttpStatusCode())
                .type(categorizeErrorType(ex.getErrorCode().getHttpStatus()))
                .code(ex.getCode())
                .message(ex.getMessage())
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(response);
    }

    /**
     * Special handling for ValidationException to include field errors.
     * This provides more specific handling than the generic ApplicationException handler.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        log.warn("Validation exception [traceId={}]: code={}, fieldErrors={}",
                traceId, ex.getCode(), ex.getFieldErrors());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(ex.getHttpStatusCode())
                .type("VALIDATION_ERROR")
                .code(ex.getCode())
                .message(ex.getMessage())
                .fieldErrors(ex.hasFieldErrors() ? ex.getFieldErrors() : null)
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(response);
    }

    /**
     * Special handling for BusinessValidationException to include detail.
     */
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessValidationException(
            BusinessValidationException ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        log.warn("Business validation exception [traceId={}]: message={}, detail={}",
                traceId, ex.getMessage(), ex.getDetail());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .type("BUSINESS_ERROR")
                .code(ex.getCode())
                .message(ex.getMessage())
                .detail(ex.getDetail())
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        String message = "Request body is missing or malformed";
        String detail = null;

        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Required request body is missing")) {
                message = "Request body is required";
                detail = "The request must include a JSON body";
            } else if (ex.getMessage().contains("JSON parse error")) {
                message = "Invalid JSON format";
                detail = "The request body contains malformed JSON";
            } else if (ex.getMessage().contains("Cannot deserialize")) {
                message = "Invalid request format";
                detail = "The request body structure does not match the expected format";
            }
        }

        log.warn("HTTP message not readable [traceId={}]: path={}, message={}",
                traceId, path, ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .type("VALIDATION_ERROR")
                .code(ValidationCodes.VALIDATION_FAILED.getCode())
                .message(message)
                .detail(detail)
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // ========================================================================
    // SPRING VALIDATION EXCEPTION HANDLERS
    // ========================================================================

    /**
     * Handles validation errors from @Valid annotation on request bodies.
     * These are thrown when DTO validation constraints fail (e.g., @NotBlank, @Email).
     * <p>
     * We extract field-level errors so clients can display them inline with form fields.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        // Extract all field errors into a map
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        log.warn("Method argument validation failed [traceId={}]: path={}, errors={}",
                traceId, path, fieldErrors);

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .type("VALIDATION_ERROR")
                .code(ValidationCodes.VALIDATION_FAILED.getCode())
                .message("Request validation failed")
                .fieldErrors(fieldErrors)
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handles constraint violations from Bean Validation on path/query parameters.
     * Thrown when @Validated class-level annotation catches constraint violations.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        // Extract violations into field error map
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            // Get the last part of the property path (the actual field name)
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.contains(".")
                    ? propertyPath.substring(propertyPath.lastIndexOf('.') + 1)
                    : propertyPath;
            fieldErrors.put(fieldName, violation.getMessage());
        });

        log.warn("Constraint violation [traceId={}]: path={}, errors={}",
                traceId, path, fieldErrors);

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .type("VALIDATION_ERROR")
                .code(ValidationCodes.VALIDATION_FAILED.getCode())
                .message("Constraint validation failed")
                .fieldErrors(fieldErrors)
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // ========================================================================
    // JWT / SECURITY EXCEPTION HANDLERS
    // ========================================================================

    /**
     * Handles expired JWT tokens specifically.
     * Provides a clear message so the client knows to refresh or re-authenticate.
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(
            ExpiredJwtException ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        log.warn("JWT token expired [traceId={}]: path={}", traceId, path);

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .type("AUTHENTICATION_ERROR")
                .code(AuthenticationCodes.TOKEN_EXPIRED.getCode())
                .message("Your session has expired. Please log in again.")
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    /**
     * Handles other JWT-related exceptions (malformed, invalid signature, etc.).
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(
            JwtException ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        log.warn("JWT exception [traceId={}]: path={}, message={}",
                traceId, path, ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .type("AUTHENTICATION_ERROR")
                .code(AuthenticationCodes.TOKEN_INVALID.getCode())
                .message("Invalid authentication token")
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // ========================================================================
    // DATABASE EXCEPTION HANDLERS
    // ========================================================================

    /**
     * Handles database constraint violations (unique constraints, foreign keys, etc.).
     * We try to extract meaningful info from the exception to provide helpful messages.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        // Attempt to provide a more helpful message based on the constraint
        String message = "Data integrity constraint violated";
        String detail = null;
        String code = DatabaseCodes.DATA_INTEGRITY_VIOLATION.getCode();

        if (ex.getMessage() != null) {
            String rootMessage = ex.getMessage().toLowerCase();

            if (rootMessage.contains("email")) {
                if (rootMessage.contains("duplicate") || rootMessage.contains("unique")) {
                    message = "Email already exists";
                    code = ResourceCodes.EMAIL_TAKEN.getCode();
                } else if (rootMessage.contains("null")) {
                    message = "Email is required";
                    code = ValidationCodes.FIELD_REQUIRED.getCode();
                }
            } else if (rootMessage.contains("username")) {
                if (rootMessage.contains("duplicate") || rootMessage.contains("unique")) {
                    message = "Username already taken";
                    code = ResourceCodes.USERNAME_TAKEN.getCode();
                }
            }

            detail = extractConstraintDetail(ex);
        }

        log.error("Data integrity violation [traceId={}]: path={}, message={}",
                traceId, path, ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .type("DATABASE_ERROR")
                .code(code)
                .message(message)
                .detail(detail)
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // ========================================================================
    // GENERAL EXCEPTION HANDLERS
    // ========================================================================

    /**
     * Handles illegal argument exceptions.
     * These typically indicate programming errors or invalid API usage.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        log.warn("Illegal argument [traceId={}]: path={}, message={}",
                traceId, path, ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .type("CLIENT_ERROR")
                .code(ValidationCodes.INVALID_INPUT.getCode())
                .message(ex.getMessage())
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Catch-all handler for any unexpected exceptions.
     * <p>
     * IMPORTANT: We log the full stack trace for debugging but never expose
     * internal details in the response. This prevents information leakage.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtExceptions(
            Exception ex,
            WebRequest request) {

        String traceId = generateTraceId();
        String path = extractPath(request);

        // Log full exception for debugging - this is critical for production troubleshooting
        log.error("Unexpected exception [traceId={}]: path={}", traceId, path, ex);

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .type("INTERNAL_ERROR")
                .code(SystemCodes.INTERNAL_ERROR.getCode())
                .message("An unexpected error occurred. Please try again later.")
                .path(path)
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================

    /**
     * Extracts the request path from WebRequest.
     * The description typically contains "uri=/path/to/resource".
     */
    private String extractPath(WebRequest request) {
        String description = request.getDescription(false);
        return description.replace("uri=", "");
    }

    /**
     * Generates a unique trace ID for error correlation.
     * This can be used to find related log entries across services.
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Categorizes HTTP status into a human-readable error type.
     * This helps clients quickly understand what category of error occurred.
     */
    private String categorizeErrorType(HttpStatus status) {
        if (status.is4xxClientError()) {
            return switch (status) {
                case UNAUTHORIZED -> "AUTHENTICATION_ERROR";
                case FORBIDDEN -> "AUTHORIZATION_ERROR";
                case NOT_FOUND -> "NOT_FOUND";
                case CONFLICT -> "CONFLICT";
                case UNPROCESSABLE_ENTITY -> "BUSINESS_ERROR";
                default -> "CLIENT_ERROR";
            };
        } else if (status.is5xxServerError()) {
            return "INTERNAL_ERROR";
        }
        return "UNKNOWN_ERROR";
    }

    /**
     * Extracts constraint details from database exceptions.
     * Attempts to find PostgreSQL-specific "Detail:" section.
     */
    private String extractConstraintDetail(DataIntegrityViolationException ex) {
        if (ex.getRootCause() != null && ex.getRootCause().getMessage() != null) {
            String rootMessage = ex.getRootCause().getMessage();

            // PostgreSQL includes a "Detail:" section with helpful info
            if (rootMessage.contains("Detail:")) {
                int detailIndex = rootMessage.indexOf("Detail:");
                int endIndex = rootMessage.indexOf("\n", detailIndex);
                if (endIndex == -1) {
                    endIndex = rootMessage.length();
                }
                return rootMessage.substring(detailIndex + 7, endIndex).trim();
            }
        }
        return null;
    }
}