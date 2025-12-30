package com.nextepisode.tmdb_service.exception;

public class TmdbApiException extends ApplicationException {

    public TmdbApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TmdbApiException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    public TmdbApiException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode, details, cause);
    }
}