package com.ccs.erp.core.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class RepositoryDataIntegrityViolationException extends BusinessLogicException {

    public RepositoryDataIntegrityViolationException(String message) {
        super(message);
    }

    public RepositoryDataIntegrityViolationException(String message, Throwable cause) {
        super(message, ExceptionUtils.getRootCause(cause));
    }
}