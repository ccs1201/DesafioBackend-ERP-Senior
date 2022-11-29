package com.ccs.erp.core.exception;

public class ItemException extends BusinessLogicException {

    public ItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemException(String message) {
        super(message);
    }
}
