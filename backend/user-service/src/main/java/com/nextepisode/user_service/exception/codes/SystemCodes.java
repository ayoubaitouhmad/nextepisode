package com.nextepisode.user_service.exception.codes;

import org.springframework.http.HttpStatus;

/**
 * System error codes for internal server and infrastructure issues.
 * These represent unexpected errors and system-level problems.
 * 
 * Prefix: SYS_xxx
 */
public enum SystemCodes implements Code {
    
    // General system errors
    INTERNAL_ERROR("SYS_001", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("SYS_002", "Service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    
    // Configuration errors
    CONFIGURATION_ERROR("SYS_100", "System configuration error: {0}", HttpStatus.INTERNAL_SERVER_ERROR),
    MISSING_CONFIGURATION("SYS_101", "Required configuration missing: {0}", HttpStatus.INTERNAL_SERVER_ERROR),
    
    // Resource errors (system resources, not business resources)
    OUT_OF_MEMORY("SYS_200", "System out of memory", HttpStatus.INTERNAL_SERVER_ERROR),
    DISK_FULL("SYS_201", "Disk storage full", HttpStatus.INSUFFICIENT_STORAGE),
    FILE_IO_ERROR("SYS_202", "File I/O error: {0}", HttpStatus.INTERNAL_SERVER_ERROR),
    
    // Communication errors
    TIMEOUT("SYS_300", "Operation timeout", HttpStatus.REQUEST_TIMEOUT),
    CIRCUIT_BREAKER_OPEN("SYS_301", "Service circuit breaker is open", HttpStatus.SERVICE_UNAVAILABLE),
    
    // Feature flags
    FEATURE_NOT_ENABLED("SYS_400", "Feature not enabled: {0}", HttpStatus.NOT_IMPLEMENTED),
    MAINTENANCE_MODE("SYS_401", "System is under maintenance", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    SystemCodes(String code, String messageTemplate, HttpStatus httpStatus) {
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
