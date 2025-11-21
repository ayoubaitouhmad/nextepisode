package com.nextepisode.auth_service.exception;


import org.springframework.http.HttpStatus;

/**
 * Centralized error code enumeration. All possible errors are defined here.
 * Adding new errors is as simple as adding an enum constant.
 * This promotes discoverability and makes error handling explicit.
 */
public enum ErrorCode {

    // Authentication errors (4xx)
    USER_NOT_FOUND(
            "AUTH_001",
            "User not found with username: {0}",
            HttpStatus.NOT_FOUND
    ),
    INVALID_CREDENTIALS(
            "AUTH_002",
            "Invalid username or password",
            HttpStatus.UNAUTHORIZED
    ),
    USERNAME_ALREADY_EXISTS(
            "AUTH_003",
            "Username already exists: {0}",
            HttpStatus.CONFLICT
    ),
    EMAIL_ALREADY_EXISTS(
            "AUTH_004",
            "Email already registered: {0}",
            HttpStatus.CONFLICT
    ),
    INVALID_EMAIL_FORMAT(
            "AUTH_005",
            "Invalid email format: {0}",
            HttpStatus.BAD_REQUEST
    ),
    WEAK_PASSWORD(
            "AUTH_006",
            "Password does not meet security requirements",
            HttpStatus.BAD_REQUEST
    ),

    // Database errors (5xx)
    DATABASE_ERROR(
            "DB_001",
            "Database operation failed",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),

    // General errors (5xx)
    INTERNAL_ERROR(
            "SYS_001",
            "An unexpected error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR
    );

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