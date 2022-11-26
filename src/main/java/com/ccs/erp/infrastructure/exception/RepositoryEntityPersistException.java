package com.ccs.erp.infrastructure.exception;

public class RepositoryEntityPersistException extends BusinessLogicException {
    public RepositoryEntityPersistException(String message) {
        super(message);
    }

    public RepositoryEntityPersistException(String message, Throwable cause) {
        super(message, cause);
    }
}