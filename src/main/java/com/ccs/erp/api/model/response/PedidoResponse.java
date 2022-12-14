package com.ccs.erp.api.model.response;

import com.ccs.erp.domain.entity.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;


@Getter
@Setter
@Schema(name = "Pedido Response",description = "Modelo de resposta de Pedido")
public class PedidoResponse extends RepresentationModel<PedidoResponse> {

    private UUID id;

    private Collection<ItemPedidoResponse> itensPedido;

    private BigDecimal valorTotalItens;

    private BigDecimal valorTotalDesconto;

    private BigDecimal valorTotalPedido;

    private StatusPedido statusPedido;

    private int percentualDesconto;

    private String observacao;

    private OffsetDateTime dataPedido;

    private OffsetDateTime ultimaAtualizacao;
}
