package com.nextepisode.user_service.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Exception for input validation errors.
 *
 * This handles validation constraint violations and supports:
 * - Simple validation errors: "Email format is invalid"
 * - Field-level errors: {"email": "Invalid format", "username": "Too short"}
 *
 * Field-level errors are particularly useful for forms where clients need
 * to display specific errors next to the relevant input fields.
 *
 * Example usage:
 *   // Single field error
 *   throw new ValidationException(ErrorCode.INVALID_EMAIL, "email", "Must be a valid email");
 *
 *   // Multiple field errors
 *   Map<String, String> errors = Map.of(
 *       "email", "Invalid format",
 *       "password", "Too weak"
 *   );
 *   throw new ValidationException(ErrorCode.VALIDATION_FAILED, errors);
 */
@Getter
public class ValidationException extends ApplicationException {

    // Immutable map of field-level validation errors
    private final Map<String, String> fieldErrors;

    /**
     * Creates a validation exception without field-level details.
     * Use when the error applies to the request as a whole.
     *
     * @param errorCode   The validation error code
     * @param messageArgs Arguments to format the error message
     */
    public ValidationException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode, messageArgs);
        this.fieldErrors = Collections.emptyMap();
    }

    /**
     * Creates a validation exception for a single field.
     * Most common usage for field-specific validation.
     *
     * @param errorCode  The validation error code
     * @param fieldName  The field that failed validation
     * @param fieldError The error message for that field
     */
    public ValidationException(ErrorCode errorCode, String fieldName, String fieldError) {
        super(errorCode);
        this.fieldErrors = Map.of(fieldName, fieldError);
    }

    /**
     * Creates a validation exception with multiple field errors.
     * Use when multiple fields fail validation simultaneously.
     *
     * @param errorCode   The validation error code
     * @param fieldErrors Map of field names to their error messages
     */
    public ValidationException(ErrorCode errorCode, Map<String, String> fieldErrors) {
        super(errorCode);
        // Create defensive copy to ensure immutability
        this.fieldErrors = fieldErrors != null
                ? Collections.unmodifiableMap(new HashMap<>(fieldErrors))
                : Collections.emptyMap();
    }

    /**
     * Creates a validation exception with cause for exception chaining.
     *
     * @param errorCode   The validation error code
     * @param cause       The underlying exception
     * @param messageArgs Arguments to format the error message
     */
    public ValidationException(ErrorCode errorCode, Throwable cause, Object... messageArgs) {
        super(errorCode, cause, messageArgs);
        this.fieldErrors = Collections.emptyMap();
    }

    /**
     * Checks if this exception contains field-level errors.
     * Useful for conditional handling in the exception handler.
     *
     * @return true if there are field-level errors
     */
    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }
}