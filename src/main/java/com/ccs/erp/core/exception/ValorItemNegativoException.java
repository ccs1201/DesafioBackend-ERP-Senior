package com.ccs.erp.core.exception;

public class ValorItemNegativoException extends BusinessLogicException {
    public ValorItemNegativoException(String message) {
        super(message);
    }

    public ValorItemNegativoException(String message, Throwable cause) {
        super(message, cause);
    }
}
