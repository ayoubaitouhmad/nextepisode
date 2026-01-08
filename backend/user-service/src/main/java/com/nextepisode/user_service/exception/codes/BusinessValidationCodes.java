package com.nextepisode.user_service.exception.codes;

import org.springframework.http.HttpStatus;

/**
 * Business validation error codes for domain/business rule violations.
 * These represent logical errors that require business context to detect.
 *
 * Prefix: BUS_xxx
 *
 * Examples:
 * - Cannot perform action due to current state
 * - Business constraints violated
 * - Domain-specific rules not met
 */
public enum BusinessValidationCodes implements Code {

    // General business rules
    BUSINESS_RULE_VIOLATION("BUS_001", "Business rule violation: {0}", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_STATE_TRANSITION("BUS_002", "Cannot transition from {0} to {1}", HttpStatus.UNPROCESSABLE_ENTITY),

    // User-related business rules
    USER_REQUIRED("BUS_100", "User must be specified for this operation", HttpStatus.UNPROCESSABLE_ENTITY),
    USER_NOT_ACTIVE("BUS_101", "User account is not active", HttpStatus.UNPROCESSABLE_ENTITY),
    USER_SUSPENDED("BUS_102", "User account is suspended", HttpStatus.FORBIDDEN),

    // Movie-related business rules
    MOVIE_REQUIRED("BUS_200", "Movie must be specified for this operation", HttpStatus.UNPROCESSABLE_ENTITY),
    MOVIE_ALREADY_IN_LIST("BUS_201", "Movie is already in {0} list", HttpStatus.CONFLICT),
    MOVIE_NOT_IN_LIST("BUS_202", "Movie is not in {0} list", HttpStatus.NOT_FOUND),
    DUPLICATE_MOVIE_STATUS("BUS_203", "Movie status already exists for this user", HttpStatus.CONFLICT),

    // Operation constraints
    OPERATION_NOT_ALLOWED("BUS_300", "Operation not allowed: {0}", HttpStatus.FORBIDDEN),
    INSUFFICIENT_PERMISSIONS("BUS_301", "Insufficient permissions for this operation", HttpStatus.FORBIDDEN),
    RATE_LIMIT_EXCEEDED("BUS_302", "Rate limit exceeded. Try again in {0} seconds", HttpStatus.TOO_MANY_REQUESTS),

    // Data constraints
    DUPLICATE_ENTRY("BUS_400", "Duplicate entry: {0}", HttpStatus.CONFLICT),
    DEPENDENT_RESOURCES_EXIST("BUS_401", "Cannot delete: dependent resources exist", HttpStatus.CONFLICT);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    BusinessValidationCodes(String code, String messageTemplate, HttpStatus httpStatus) {
        this.code = code;
        this.messageTemplate = messageTemplate;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessageTemplate() {
        return messageTemplate;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
