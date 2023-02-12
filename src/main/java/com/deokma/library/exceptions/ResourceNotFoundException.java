package com.deokma.library.exceptions;

/**
 * @author Denis Popolamov
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}