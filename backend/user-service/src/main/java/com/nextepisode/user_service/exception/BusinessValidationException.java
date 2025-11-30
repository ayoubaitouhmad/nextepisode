package com.nextepisode.user_service.exception;

import lombok.Getter;

/**
 * Exception for business rule violations.
 * <p>
 * Unlike ValidationException (which handles input format validation),
 * BusinessValidationException is for domain-specific rule violations that
 * require business logic to detect.
 * <p>
 * Examples of business validation:
 * - "Cannot cancel an order that has already shipped"
 * - "User has exceeded their monthly transaction limit"
 * - "Account must have a positive balance for this operation"
 * <p>
 * The key distinction is that input validation catches malformed data,
 * while business validation catches logically invalid operations.
 */
@Getter
public class BusinessValidationException extends ApplicationException {

    // More specific detail about the violation (optional)
    private final String detail;

    /**
     * Creates a business validation exception with just a message.
     * The message is passed to the parent and becomes the exception message.
     *
     * @param message Human-readable description of the rule violation
     */
    public BusinessValidationException(String message) {
        super(ErrorCode.BUSINESS_RULE_VIOLATION, message);
        this.detail = null;
    }

    /**
     * Creates a business validation exception with message and detail.
     * Detail provides additional context that might help resolve the issue.
     *
     * @param message Brief description of the violation
     * @param detail  More specific information or suggestions
     */
    public BusinessValidationException(String message, String detail) {
        super(ErrorCode.BUSINESS_RULE_VIOLATION, message);
        this.detail = detail;
    }

    /**
     * Creates a business validation exception with a specific error code.
     * Use when you have defined specific ErrorCodes for certain business rules.
     *
     * @param errorCode   The specific error code for this rule
     * @param messageArgs Arguments to format the error code's message template
     */
    public BusinessValidationException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode, messageArgs);
        this.detail = null;
    }

    /**
     * Creates a business validation exception with code, detail, and args.
     *
     * @param errorCode   The specific error code
     * @param detail      Additional context
     * @param messageArgs Arguments for the message template
     */
    public BusinessValidationException(ErrorCode errorCode, String detail, Object... messageArgs) {
        super(errorCode, messageArgs);
        this.detail = detail;
    }

    /**
     * Creates a business validation exception wrapping another exception.
     *
     * @param message Brief description of the violation
     * @param cause   The underlying exception
     */
    public BusinessValidationException(String message, Throwable cause) {
        super(ErrorCode.BUSINESS_RULE_VIOLATION, cause, message);
        this.detail = null;
    }
}