package com.ccs.erp.core.exception;

public class RepositoryEntityUpdateException extends BusinessLogicException {

    public RepositoryEntityUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryEntityUpdateException(String message) {
        super(message);
    }
}
