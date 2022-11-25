package com.ccs.erp.infrastructure.exception;

import java.util.UUID;

public class ItemNaoEncontradoException extends ItemException {

    public ItemNaoEncontradoException(UUID uuid) {
        super(String.format("item de código %s, não encontrado.", uuid));
    }
}
