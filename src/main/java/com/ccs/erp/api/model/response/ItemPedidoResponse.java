package com.ccs.erp.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;


@Getter
@Setter
@Schema(name = "Item Pedido Response",description = "Modelo de resposta para Item Pedido")
public class ItemPedidoResponse extends RepresentationModel<ItemPedidoResponse> {

    private ItemResponse item;

    private BigDecimal valorUnitario;

    private int quantidade;

    private BigDecimal valorTotalItem;

    private BigDecimal valorDesconto;

}
