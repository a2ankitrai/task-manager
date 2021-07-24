package com.ank.exception;

public class CapacityBreachedException extends RuntimeException {

    public CapacityBreachedException(String message) {
        super(message);
    }

}
