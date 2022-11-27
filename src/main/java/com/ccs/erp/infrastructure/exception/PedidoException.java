package com.ccs.erp.infrastructure.exception;

public class PedidoException extends BusinessLogicException {

    public PedidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public PedidoException(String message) {
        super(message);
    }

    public PedidoException() {
    }
}
