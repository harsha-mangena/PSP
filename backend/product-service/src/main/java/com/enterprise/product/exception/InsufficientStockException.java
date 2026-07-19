package com.enterprise.product.exception;

/**
 * Raised when a stock reduction would take a product below zero.
 * Status mapping lives in GlobalExceptionHandler.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Integer productId, Integer requested, Integer available) {
        super("Insufficient stock for product " + productId
                + ": requested " + requested + ", available " + available);
    }
}
