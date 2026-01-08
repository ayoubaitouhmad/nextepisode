package com.nextepisode.user_service.exception.codes;

import org.springframework.http.HttpStatus;

/**
 * Resource error codes for resource management operations.
 * These handle resource existence, conflicts, and state issues.
 * 
 * Prefix: RES_xxx
 */
public enum ResourceCodes implements Code {
    
    // General resource errors
    RESOURCE_NOT_FOUND("RES_001", "{0} not found with {1}: {2}", HttpStatus.NOT_FOUND),
    RESOURCE_ALREADY_EXISTS("RES_002", "{0} already exists: {1}", HttpStatus.CONFLICT),
    RESOURCE_CONFLICT("RES_003", "Resource conflict: {0}", HttpStatus.CONFLICT),
    
    // User resources
    USER_NOT_FOUND("RES_100", "User not found with {0}: {1}", HttpStatus.NOT_FOUND),
    USERNAME_TAKEN("RES_101", "Username already taken: {0}", HttpStatus.CONFLICT),
    EMAIL_TAKEN("RES_102", "Email already registered: {0}", HttpStatus.CONFLICT),
    
    // Movie resources
    MOVIE_NOT_FOUND("RES_200", "Movie not found with {0}: {1}", HttpStatus.NOT_FOUND),
    MOVIE_STATUS_NOT_FOUND("RES_201", "Movie status not found for movie ID: {0}", HttpStatus.NOT_FOUND),
    
    // Generic entity resources
    ENTITY_NOT_FOUND("RES_900", "{0} with ID {1} not found", HttpStatus.NOT_FOUND),
    ENTITY_ALREADY_EXISTS("RES_901", "{0} with {1} {2} already exists", HttpStatus.CONFLICT);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    ResourceCodes(String code, String messageTemplate, HttpStatus httpStatus) {
        this.code = code;
        this.messageTemplate = messageTemplate;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessageTemplate() {
        return messageTemplate;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
