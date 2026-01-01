package com.nextepisode.tmdb_service.exception;


import lombok.Getter;

@Getter
public  abstract class ApplicationException extends RuntimeException {

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