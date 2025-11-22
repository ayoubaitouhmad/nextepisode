package com.nextepisode.auth_service.exception;


import java.util.HashMap;
import java.util.Map;

/**
 * Exception for input validation errors (e.g., @Email, @NotBlank, etc.).
 * Handles validation constraint violations from Jakarta Bean Validation.
 * <p>
 * Supports:
 * - Single field errors: "Email is invalid"
 * - Multiple field errors: {"email": "Invalid format", "username": "Too short"}
 */
public class ValidationException extends ApplicationException {

    private final Map<String, String> fieldErrors;

    /**
     * Single error without field-level details.
     * Used when validation fails but we don't need field-specific messages.
     */
    public ValidationException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode, messageArgs);
        this.fieldErrors = new HashMap<>();
    }

    /**
     * Single error with field-level details.
     * Used for specific field validation failures.
     *
     * @param errorCode  The error code
     * @param fieldName  The field that failed validation
     * @param fieldError The error message for that field
     */
    public ValidationException(ErrorCode errorCode, String fieldName, String fieldError) {
        super(errorCode);
        this.fieldErrors = new HashMap<>();
        this.fieldErrors.put(fieldName, fieldError);
    }

    /**
     * Multiple field errors.
     * Used when multiple fields fail validation.
     *
     * @param errorCode   The error code
     * @param fieldErrors Map of field names to error messages
     */
    public ValidationException(ErrorCode errorCode, Map<String, String> fieldErrors) {
        super(errorCode);
        this.fieldErrors = new HashMap<>(fieldErrors);
    }

    /**
     * Get field-level validation errors.
     * Useful for client to display errors next to specific fields.
     */
    public Map<String, String> getFieldErrors() {
        return new HashMap<>(fieldErrors);
    }

    /**
     * Check if this exception has field-level errors.
     */
    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }

    /**
     * Add a field error.
     */
    public void addFieldError(String fieldName, String errorMessage) {
        this.fieldErrors.put(fieldName, errorMessage);
    }
}