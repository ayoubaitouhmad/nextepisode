package com.nextepisode.user_service.exception;

import lombok.Getter;

/**
 * Exception thrown when business validation rules are violated.
 * This is used for domain-specific validation that goes beyond simple field validation.
 */
@Getter
public class BusinessValidationException extends RuntimeException {

    private final String detail;
    private final String code;

    public BusinessValidationException(String message, String code) {
        super(message);
        this.code = code;
        this.detail = null;
    }

    public BusinessValidationException(String message, String detail, String code) {
        super(message);
        this.detail = detail;
        this.code = code;
    }

    public BusinessValidationException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
        this.detail = null;
    }

    public BusinessValidationException(String message, String detail,  String code,Throwable cause) {
        super(message, cause);
        this.detail = detail;
        this.code = code;
    }
}