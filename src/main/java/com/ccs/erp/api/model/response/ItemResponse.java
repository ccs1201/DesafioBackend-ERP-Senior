package com.ccs.erp.api.model.response;

import com.ccs.erp.domain.entity.TipoItem;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Modelo de resposta de Item")
@Relation("item")
@JsonRootName("Item")
public class ItemResponse extends RepresentationModel<ItemResponse> {

    private UUID id;

    private String nome;

    private TipoItem tipoItem;

    private BigDecimal valor;

    private Boolean ativo;
}
