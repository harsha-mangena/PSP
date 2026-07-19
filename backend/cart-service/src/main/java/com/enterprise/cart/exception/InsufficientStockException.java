package com.enterprise.cart.exception;

/**
 * Status mapping lives in GlobalExceptionHandler.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Integer productId, Integer requested, Integer available) {
        super(buildMessage(productId, requested, available));
    }

    private static String buildMessage(Integer productId, Integer requested, Integer available) {
        String base = "Insufficient stock for product " + productId + ": requested " + requested;
        // product-service reports the shortfall via a 409 without a body, so the
        // available count is not always known on this side.
        return available == null ? base : base + ", available " + available;
    }
}
