package com.ccs.erp.api.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "Item Input", description = "Modelo de entrada de Item")
public class ItemInput {
    @NotNull
    private String nome;
    @Positive
    @NotNull
    private BigDecimal valor;
    @NotNull
    private Boolean ativo;
}
