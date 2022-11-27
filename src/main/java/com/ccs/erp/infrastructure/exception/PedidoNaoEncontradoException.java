package com.ccs.erp.infrastructure.exception;

import java.util.UUID;

public class PedidoNaoEncontradoException extends RepositoryEntityNotFoundException {

    private static final String msg = "Pedido código %s, não existe.";

    public PedidoNaoEncontradoException(UUID id) {
        super(String.format(msg, id));
    }
}
