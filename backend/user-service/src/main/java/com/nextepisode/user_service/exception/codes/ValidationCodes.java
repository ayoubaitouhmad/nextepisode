package com.nextepisode.user_service.exception.codes;

import org.springframework.http.HttpStatus;

/**
 * Validation error codes for input format validation.
 * These are technical validation errors (format, type, required fields).
 *
 * Prefix: VAL_xxx
 */
public enum ValidationCodes implements Code {

    // General validation
    VALIDATION_FAILED("VAL_001", "Validation failed", HttpStatus.BAD_REQUEST),
    INVALID_INPUT("VAL_002", "Invalid input: {0}", HttpStatus.BAD_REQUEST),

    // Field validation
    FIELD_REQUIRED("VAL_100", "Required field is missing: {0}", HttpStatus.BAD_REQUEST),
    FIELD_TOO_SHORT("VAL_101", "Field {0} is too short (minimum: {1})", HttpStatus.BAD_REQUEST),
    FIELD_TOO_LONG("VAL_102", "Field {0} is too long (maximum: {1})", HttpStatus.BAD_REQUEST),

    // Format validation
    INVALID_EMAIL("VAL_200", "Invalid email format: {0}", HttpStatus.BAD_REQUEST),
    INVALID_DATE("VAL_201", "Invalid date format: {0}", HttpStatus.BAD_REQUEST),
    INVALID_UUID("VAL_202", "Invalid UUID format: {0}", HttpStatus.BAD_REQUEST),
    INVALID_ENUM("VAL_203", "Invalid value for {0}. Allowed values: {1}", HttpStatus.BAD_REQUEST),

    // Type validation
    INVALID_TYPE("VAL_300", "Invalid type for field {0}: expected {1}", HttpStatus.BAD_REQUEST),
    INVALID_NUMBER("VAL_301", "Invalid number format: {0}", HttpStatus.BAD_REQUEST),

    // Password validation
    WEAK_PASSWORD("VAL_400", "Password does not meet security requirements", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT("VAL_401", "Password must be at least {0} characters", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    ValidationCodes(String code, String messageTemplate, HttpStatus httpStatus) {
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
