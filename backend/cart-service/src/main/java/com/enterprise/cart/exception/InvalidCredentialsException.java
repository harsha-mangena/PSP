package com.enterprise.cart.exception;

/**
 * Status mapping lives in GlobalExceptionHandler.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        // Deliberately vague: never reveal whether the username or the password
        // was the part that was wrong.
        super("Invalid username or password");
    }
}
