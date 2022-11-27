package com.ccs.erp.api.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.util.Collection;

@Getter
@Setter
@Schema(name = "Pedido Input", description = "Modelo de entrada de um pedido")
public class PedidoInput {
    @Valid
    private Collection<ItemPedidoInput> itensPedido;

    @Length(max = 150)
    private String observacao;
}
