package com.enterprise.cart.exception;

/**
 * Status mapping lives in GlobalExceptionHandler.
 */
public class EmptyCartException extends RuntimeException {

    public EmptyCartException(String userId) {
        super("Cannot check out an empty cart for user: " + userId);
    }
}
