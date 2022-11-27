package com.ccs.erp.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Tipo Item", description = "Enum que representa o tipo do Item", enumAsRef = true)
public enum TipoItem {
    SERVICO, PRODUTO
}
