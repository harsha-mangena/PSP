package com.enterprise.cart.exception;

/**
 * Status mapping lives in GlobalExceptionHandler.
 */
public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(Integer itemId) {
        super("Cart item not found: " + itemId);
    }
}
