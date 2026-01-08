package com.nextepisode.user_service.exception.exceptions;

import com.nextepisode.user_service.exception.codes.Code;
import lombok.Getter;

/**
 * Base exception class for all application-specific exceptions.
 *
 * This abstract class provides a foundation for a consistent exception hierarchy:
 * - All custom exceptions extend this class
 * - Each exception carries an ErrorCode, ensuring consistent error handling
 * - HTTP status and error messages are derived from the ErrorCode
 *
 * Why extend RuntimeException?
 * - Avoids cluttering method signatures with checked exception declarations
 * - Better suited for errors that typically can't be recovered from gracefully
 * - Works well with Spring's exception handling mechanism
 */
@Getter
public abstract class ApplicationException extends RuntimeException {

    private final Code errorCode;
    private final Object[] messageArgs;

    /**
     * Creates exception with error code and optional message arguments.
     *
     * @param errorCode   The error code defining this exception's type
     * @param messageArgs Arguments to format the error message template
     */
    protected ApplicationException(Code errorCode, Object... messageArgs) {
        super(errorCode.getMessage(messageArgs));
        this.errorCode = errorCode;
        this.messageArgs = messageArgs != null ? messageArgs : new Object[0];
    }

    /**
     * Creates exception with error code, cause, and optional message arguments.
     * Use this when wrapping another exception to preserve the stack trace.
     *
     * @param errorCode   The error code defining this exception's type
     * @param cause       The underlying exception that caused this one
     * @param messageArgs Arguments to format the error message template
     */
    protected ApplicationException(Code errorCode, Throwable cause, Object... messageArgs) {
        super(errorCode.getMessage(messageArgs), cause);
        this.errorCode = errorCode;
        this.messageArgs = messageArgs != null ? messageArgs : new Object[0];
    }

    /**
     * Gets the HTTP status code associated with this error.
     * Derived from the ErrorCode, ensuring consistency.
     */
    public int getHttpStatusCode() {
        return errorCode.getHttpStatus().value();
    }

    /**
     * Gets the string error code (e.g., "AUTH_001").
     * Useful for client-side error handling and logging.
     */
    public String getCode() {
        return errorCode.getCode();
    }
}