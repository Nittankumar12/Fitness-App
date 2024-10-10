package com.fitness.user_profile.exception;

/**
 * Custom exception for fitness goals not found errors.
 */
public class FitnessGoalsNotFoundException extends RuntimeException {

    /**
     * Constructs a new FitnessGoalsNotFoundException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     */
    public FitnessGoalsNotFoundException(String message) {
        super(message);
    }
}
