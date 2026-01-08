package com.nextepisode.user_service.exception.codes;

import org.springframework.http.HttpStatus;

/**
 * Database error codes for data persistence and integrity issues.
 * These handle database operations, constraints, and connectivity.
 * 
 * Prefix: DB_xxx
 */
public enum DatabaseCodes implements Code {
    
    // General database errors
    DATABASE_ERROR("DB_001", "Database operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_CONNECTION_ERROR("DB_002", "Database connection failed", HttpStatus.SERVICE_UNAVAILABLE),
    QUERY_TIMEOUT("DB_003", "Database query timeout", HttpStatus.REQUEST_TIMEOUT),
    
    // Data integrity errors
    DATA_INTEGRITY_VIOLATION("DB_100", "Data integrity constraint violated", HttpStatus.CONFLICT),
    UNIQUE_CONSTRAINT_VIOLATION("DB_101", "Duplicate value for unique field: {0}", HttpStatus.CONFLICT),
    FOREIGN_KEY_VIOLATION("DB_102", "Foreign key constraint violated: {0}", HttpStatus.CONFLICT),
    NULL_CONSTRAINT_VIOLATION("DB_103", "Required field cannot be null: {0}", HttpStatus.BAD_REQUEST),
    
    // Transaction errors
    TRANSACTION_FAILED("DB_200", "Transaction failed: {0}", HttpStatus.INTERNAL_SERVER_ERROR),
    DEADLOCK_DETECTED("DB_201", "Deadlock detected, operation rolled back", HttpStatus.CONFLICT),
    OPTIMISTIC_LOCK_FAILURE("DB_202", "Resource was modified by another transaction", HttpStatus.CONFLICT),
    
    // Migration/Schema errors
    SCHEMA_ERROR("DB_300", "Database schema error: {0}", HttpStatus.INTERNAL_SERVER_ERROR),
    MIGRATION_FAILED("DB_301", "Database migration failed", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String messageTemplate;
    private final HttpStatus httpStatus;

    DatabaseCodes(String code, String messageTemplate, HttpStatus httpStatus) {
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
