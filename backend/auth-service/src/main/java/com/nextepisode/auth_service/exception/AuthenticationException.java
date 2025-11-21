package com.nextepisode.auth_service.exception;


/**
 * Exception for authentication-related errors (login, registration, credentials).
 * Specific to the auth service domain.
 */
public class AuthenticationException extends ApplicationException {

    public AuthenticationException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode, messageArgs);
    }

    public AuthenticationException(ErrorCode errorCode, Throwable cause, Object... messageArgs) {
        super(errorCode, cause, messageArgs);
    }
}