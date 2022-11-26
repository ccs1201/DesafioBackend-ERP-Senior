package com.ccs.erp.infrastructure.exception;

public class RepositoryEntityInUseException extends BusinessLogicException {
    public RepositoryEntityInUseException(String message, Throwable cause) {
        super(message,cause);
    }

    public RepositoryEntityInUseException(String message) {
        super(message);
    }
}