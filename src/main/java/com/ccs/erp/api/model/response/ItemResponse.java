package com.ccs.erp.api.model.response;

import com.ccs.erp.domain.entity.TipoItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "Item Response", description = "Modelo de resposta de Item")
public class ItemResponse {

    private UUID id;

    private String nome;

    private TipoItem tipoItem;

    private BigDecimal valor;

    private Boolean ativo;
}
