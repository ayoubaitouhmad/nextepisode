package com.nextepisode.user_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Centralized error codes for the entire application.
 *
 * Design principles:
 * - Each error has a unique code for easy tracking and debugging
 * - Message templates support parameterized values for context
 * - HTTP status is tied to the error, ensuring consistent responses
 *
 * Naming convention for codes:
 * - AUTH_xxx: Authentication/authorization errors
 * - VAL_xxx: Validation errors
 * - DB_xxx: Database errors
 * - RES_xxx: Resource errors (not found, conflict, etc.)
 * - SYS_xxx: System/internal errors
 */
public enum ErrorCode {

    // ==================== Authentication Errors ====================
    USER_NOT_FOUND("AUTH_001", "User not found with {0}: {1}", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("AUTH_002", "Invalid username or password", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("AUTH_003", "Authentication token has expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("AUTH_004", "Authentication token is invalid", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("AUTH_005", "Access denied to this resource", HttpStatus.FORBIDDEN),

    // ==================== Validation Errors ====================
    VALIDATION_FAILED("VAL_001", "Validation failed", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL("VAL_002", "Invalid email format: {0}", HttpStatus.BAD_REQUEST),
    FIELD_REQUIRED("VAL_003", "Required field is missing: {0}", HttpStatus.BAD_REQUEST),
    INVALID_INPUT("VAL_004", "Invalid input: {0}", HttpStatus.BAD_REQUEST),
    WEAK_PASSWORD("VAL_005", "Password does not meet security requirements", HttpStatus.BAD_REQUEST),

    // ==================== Resource Errors ====================
    RESOURCE_NOT_FOUND("RES_001", "{0} not found with {1}: {2}", HttpStatus.NOT_FOUND),
    RESOURCE_ALREADY_EXISTS("RES_002", "{0} already exists: {1}", HttpStatus.CONFLICT),
    USERNAME_TAKEN("RES_003", "Username already taken: {0}", HttpStatus.CONFLICT),
    EMAIL_TAKEN("RES_004", "Email already registered: {0}", HttpStatus.CONFLICT),

    // ==================== Business Rule Errors ====================
    BUSINESS_RULE_VIOLATION("BUS_001", "Business rule violation: {0}", HttpStatus.UNPROCESSABLE_ENTITY),

    // ==================== Database Errors ====================
    DATABASE_ERROR("DB_001", "Database operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    DATA_INTEGRITY_VIOLATION("DB_002", "Data integrity constraint violated", HttpStatus.CONFLICT),

    // ==================== System Errors ====================
    INTERNAL_ERROR("SYS_001", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("SYS_002", "Service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE);

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
     * Placeholders like {0}, {1} are replaced with corresponding args.
     *
     * Example:
     *   RESOURCE_NOT_FOUND.getMessage("User", "id", 123)
     *   → "User not found with id: 123"
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
}