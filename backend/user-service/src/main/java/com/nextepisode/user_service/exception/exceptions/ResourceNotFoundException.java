package com.nextepisode.user_service.exception.exceptions;

import com.nextepisode.user_service.exception.codes.Code;
import com.nextepisode.user_service.exception.codes.ResourceCodes;

/**
 * Exception thrown when a requested resource cannot be found.
 * <p>
 * This is typically used in scenarios where:
 * - A user requests an entity by ID that doesn't exist
 * - A lookup by unique field (email, username) returns no results
 * - An operation references an entity that has been deleted
 * <p>
 * Example usage:
 * throw new ResourceNotFoundException("User", "id", userId);
 * // Produces: "User not found with id: 123"
 */
public class ResourceNotFoundException extends ApplicationException {

    /**
     * Creates exception for a resource not found by field lookup.
     * This is the most common usage pattern.
     *
     * @param resourceName The type of resource (e.g., "User", "Order")
     * @param fieldName    The field used for lookup (e.g., "id", "email")
     * @param fieldValue   The value that was searched for
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(ResourceCodes.RESOURCE_NOT_FOUND, resourceName, fieldName, fieldValue);
    }

    /**
     * Creates exception with a custom error code.
     * Use this when you need more specific error codes (e.g., USER_NOT_FOUND).
     *
     * @param errorCode   Specific error code for this resource type
     * @param messageArgs Arguments to format the error message
     */
    public ResourceNotFoundException(Code errorCode, Object... messageArgs) {
        super(errorCode, messageArgs);
    }

    /**
     * Creates exception with underlying cause.
     * Use when the "not found" is a result of another exception.
     *
     * @param resourceName The type of resource
     * @param fieldName    The field used for lookup
     * @param fieldValue   The value searched for
     * @param cause        The underlying exception
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue, Throwable cause) {
        super(ResourceCodes.RESOURCE_NOT_FOUND, cause, resourceName, fieldName, fieldValue);
    }
}