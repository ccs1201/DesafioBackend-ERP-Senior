package com.ccs.erp.infrastructure.configuration.beanvalidation.exception;

public class FieldValidationException extends RuntimeException {
    public FieldValidationException(String message) {
        super(message);
    }
}
