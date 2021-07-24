package com.ank.exception;

public class ProcessNotExistException extends RuntimeException {

    public ProcessNotExistException() {
        super("Process does not exist in the system.");
    }

    public ProcessNotExistException(String message) {
        super(message);
    }
}
