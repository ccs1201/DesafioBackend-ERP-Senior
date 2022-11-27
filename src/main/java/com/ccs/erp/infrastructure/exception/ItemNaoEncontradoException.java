package com.ccs.erp.infrastructure.exception;

import java.util.UUID;

public class ItemNaoEncontradoException extends RepositoryEntityNotFoundException {

    private static final String msg = "Item de código %s, não existe.";

    public ItemNaoEncontradoException(UUID uuid) {

        super(String.format(msg, uuid));
    }
}
