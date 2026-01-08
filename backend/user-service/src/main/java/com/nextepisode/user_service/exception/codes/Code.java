package com.nextepisode.user_service.exception.codes;

import org.springframework.http.HttpStatus;

/**
 * Interface for all error codes across the application.
 * Each domain-specific enum implements this to ensure consistency.
 */
public interface Code {

    String getCode();

    String getMessageTemplate();

    HttpStatus getHttpStatus();

    /**
     * Formats the message template with provided arguments.
     * Placeholders like {0}, {1} are replaced with corresponding args.
     */
    default String getMessage(Object... args) {
        if (args == null || args.length == 0) {
            return getMessageTemplate();
        }

        String message = getMessageTemplate();
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return message;
    }
}