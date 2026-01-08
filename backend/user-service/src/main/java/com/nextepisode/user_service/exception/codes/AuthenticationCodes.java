package com.nextepisode.user_service.exception.codes;

import org.springframework.http.HttpStatus;

/**
 * Authentication and authorization error codes.
 * These handle user identity verification and access control.
 *
 * Prefix: AUTH_xxx
 */
public enum AuthenticationCodes implements Code {

    // Authentication errors (who you are)
    INVALID_CREDENTIALS("AUTH_001", "Invalid username or password", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("AUTH_002", "User not found with {0}: {1}", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED("AUTH_003", "Account is locked", HttpStatus.UNAUTHORIZED),
    ACCOUNT_DISABLED("AUTH_004", "Account is disabled", HttpStatus.UNAUTHORIZED),

    // Token errors
    TOKEN_EXPIRED("AUTH_100", "Authentication token has expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("AUTH_101", "Authentication token is invalid", HttpStatus.UNAUTHORIZED),
    TOKEN_MISSING("AUTH_102", "Authentication token is missing", HttpStatus.UNAUTHORIZED),
    TOKEN_REVOKED("AUTH_103", "Authentication token has been revoked", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("AUTH_104", "Refresh token is invalid or expired", HttpStatus.UNAUTHORIZED),

    // Authorization errors (what you can do)
    ACCESS_DENIED("AUTH_200", "Access denied to this resource", HttpStatus.FORBIDDEN),
    INSUFFICIENT_PRIVILEGES("AUTH_201", "Insufficient privileges for this operation", HttpStatus.FORBIDDEN),
    RESOURCE_ACCESS_DENIED("AUTH_202", "Access denied to {0} with ID: {1}", HttpStatus.FORBIDDEN),

    // Session errors
    SESSION_EXPIRED("AUTH_300", "Session has expired", HttpStatus.UNAUTHORIZED),
    SESSION_INVALID("AUTH_301", "Invalid session", HttpStatus.UNAUTHORIZED),
    CONCURRENT_SESSION_LIMIT("AUTH_302", "Maximum concurrent sessions reached", HttpStatus.CONFLICT);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    AuthenticationCodes(String code, String messageTemplate, HttpStatus httpStatus) {
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
