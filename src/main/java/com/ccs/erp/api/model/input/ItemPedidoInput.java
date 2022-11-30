package com.ccs.erp.api.model.input;

import com.ccs.erp.api.model.input.IDs.ItemIdInput;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Schema(name = "Item Pedido Input",description = "Modelo de entrada de Item Pedido")
public class ItemPedidoInput {
    @NotNull
    @Valid
    private ItemIdInput item;
    @Positive
    private int quantidade;

}
