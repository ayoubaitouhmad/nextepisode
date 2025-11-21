package com.nextepisode.auth_service.exception;


/**
 * Base exception class for all application-specific exceptions.
 * This provides a foundation for extensible error handling across the application.
 */
public abstract class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final int httpStatusCode;
    private final Object[] messageArgs;

    public ApplicationException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode.getMessage(messageArgs));
        this.errorCode = errorCode;
        this.httpStatusCode = errorCode.getHttpStatus().value();
        this.messageArgs = messageArgs;
    }

    public ApplicationException(ErrorCode errorCode, Throwable cause, Object... messageArgs) {
        super(errorCode.getMessage(messageArgs), cause);
        this.errorCode = errorCode;
        this.httpStatusCode = errorCode.getHttpStatus().value();
        this.messageArgs = messageArgs;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getErrorName() {
        return errorCode.getCode();
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }
}