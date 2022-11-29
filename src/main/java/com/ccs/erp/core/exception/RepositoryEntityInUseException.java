package com.ccs.erp.core.exception;

public class RepositoryEntityInUseException extends BusinessLogicException {
    public RepositoryEntityInUseException(String message, Throwable cause) {
        super(message,cause);
    }

    public RepositoryEntityInUseException(String message) {
        super(message);
    }
}