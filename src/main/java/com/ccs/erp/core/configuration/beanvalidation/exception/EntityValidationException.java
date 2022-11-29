package com.ccs.erp.core.configuration.beanvalidation.exception;

import com.ccs.erp.core.exception.BusinessLogicException;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class EntityValidationException extends BusinessLogicException {

    private final BindingResult bindingResult;

    public EntityValidationException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public EntityValidationException(String message, Throwable cause, BindingResult bindingResult) {
        super(message, cause);
        this.bindingResult = bindingResult;
    }

}
