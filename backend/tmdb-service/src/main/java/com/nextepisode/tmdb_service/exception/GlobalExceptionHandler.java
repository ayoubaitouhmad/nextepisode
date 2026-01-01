package com.nextepisode.tmdb_service.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================================
    // HANDLER 1: Application Business Exceptions
    // ============================================

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException ex,
            WebRequest request) {

        ErrorResponse errorResponse = buildErrorResponse(
                ex.getErrorCode(),
                ex.getErrorCode().getHttpStatus(),
                getRequestPath(request),
                null,
                ex.getMessageArgs()
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

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(  // ✅ Renamed
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

        log.warn("Validation exception occurred: code={}, message={}, fieldErrors={}",
                ex.getErrorName(), ex.getMessage(), ex.getFieldErrors());

        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(TmdbApiException.class)
    public ResponseEntity<ErrorResponse> handleTmdbApiException(  // ✅ Renamed
                                                                  TmdbApiException ex,
                                                                  WebRequest request) {

        ErrorResponse errorResponse = buildErrorResponse(
                ex.getErrorCode(),
                ex.getErrorCode().getHttpStatus(),
                getRequestPath(request),
                null,
                ex.getMessageArgs()
        );

        log.warn("TMDB API exception occurred: code={}, message={}",
                ex.getErrorName(), ex.getMessage());

        return ResponseEntity
                .status(ex.getHttpStatusCode())
                .body(errorResponse);
    }

    // ============================================
    // HANDLER 3: Framework Validation Exceptions
    // ============================================

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();

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
                ErrorCode.VALIDATION_FIELD_REQUIRED,
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
    // HANDLER 4: Fallback Handler
    // ============================================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex,
            WebRequest request) {

        String traceId = UUID.randomUUID().toString();

        ErrorResponse errorResponse = buildErrorResponse(
                ErrorCode.GENERAL_INTERNAL_ERROR,
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

    private String categorizeErrorType(HttpStatus status) {
        if (status.is4xxClientError()) {
            return "CLIENT_ERROR";
        } else if (status.is5xxServerError()) {
            return "SERVER_ERROR";
        }
        return "UNKNOWN";
    }

    private String getRequestPath(WebRequest request) {
        try {
            return request.getDescription(false).replace("uri=", "");
        } catch (Exception e) {
            log.warn("Could not extract request path from request", e);
            return "unknown";
        }
    }
}