package com.ccs.erp.api.model.input;

import com.ccs.erp.domain.entity.TipoItem;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@JsonRootName("item")
@Getter
@Setter
@NoArgsConstructor
@Schema(name = "item Input", description = "Modelo de entrada de Item")
public class ItemInput {

    @NotNull
    private String nome;
    @Schema(enumAsRef = true)
    private TipoItem tipoItem;

    @Positive
    @NotNull
    private BigDecimal valor;

    @NotNull
    private Boolean ativo;
}
