package com.deokma.library.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;

/**
 * @author Denis Popolamov
 */
public class CustomNotFoundException extends RuntimeException {
    private String message;
    public CustomNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
