package com.enterprise.cart.exception;

/**
 * Status mapping lives in GlobalExceptionHandler.
 */
public class ProductUnavailableException extends RuntimeException {

    public ProductUnavailableException(String message) {
        super(message);
    }
}
