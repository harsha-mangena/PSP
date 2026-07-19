package com.enterprise.cart.exception;

/**
 * Status mapping lives in GlobalExceptionHandler.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Integer productId, Integer requested, Integer available) {
        super("Insufficient stock for product " + productId
                + ": requested " + requested + ", available " + available);
    }
}
