package com.ccs.erp.core.exception;

public class RepositoryEntityPersistException extends BusinessLogicException {
    public RepositoryEntityPersistException(String message) {
        super(message);
    }

    public RepositoryEntityPersistException(String message, Throwable cause) {
        super(message, cause);
    }
}