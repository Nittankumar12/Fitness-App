package com.fitness.user_profile.exception;

/**
 * Custom exception for generic errors in the application.
 */
public class GenericException extends RuntimeException {

    /**
     * Constructs a new GenericException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     */
    public GenericException(String message) {
        super(message);
    }
}
