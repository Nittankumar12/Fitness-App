package com.fitness.user_profile.advice;

import com.fitness.user_profile.exception.FitnessGoalsNotFoundException;
import com.fitness.user_profile.exception.GenericException;
import com.fitness.user_profile.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for User Profile Service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Exception handler for UserNotFoundException.
     * @param exception The exception instance.
     * @return ResponseEntity with error details.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception){
        logger.error("Exception {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Exception handler for FitnessGoalsNotFoundException.
     * @param exception The exception instance.
     * @return ResponseEntity with error details.
     */
    @ExceptionHandler(FitnessGoalsNotFoundException.class)
    public ResponseEntity<String> handleFitnessGoalsNotFoundException(FitnessGoalsNotFoundException exception){
        logger.error("Exception is: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Exception handler for GenericException.
     * @param exception The exception instance.
     * @return ResponseEntity with error details.
     */
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<String> handleGenericException(GenericException exception){
        logger.error("Ex: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    /**
     * Exception handler for all other exceptions.
     * @param exception The exception instance.
     * @return ResponseEntity with error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception exception){
        logger.error("Exception: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
