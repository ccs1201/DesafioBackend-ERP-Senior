package com.ccs.erp.core.utils.mapper;

import com.ccs.erp.api.model.input.PedidoInput;
import com.ccs.erp.api.model.response.PedidoResponse;
import com.ccs.erp.api.v1.controller.PedidoController;
import com.ccs.erp.core.utils.hateoas.LinksBuilder;
import com.ccs.erp.domain.entity.Pedido;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper extends AbstractMapper<PedidoResponse, PedidoInput, Pedido> {

    private final LinksBuilder linksBuilder;

    public PedidoMapper(LinksBuilder linksBuilder) {
        super(PedidoController.class, PedidoResponse.class);
        this.linksBuilder = linksBuilder;
    }

    @Override
    public PedidoResponse toModel(Pedido pedido) {
        var pedidoResponse = super.toModel(pedido);
        linksBuilder.linkToPedidoResponse(pedidoResponse);
        return pedidoResponse;
    }

    @Override
    public CollectionModel<PedidoResponse> toCollectionModel(Iterable<? extends Pedido> entities) {
        return super.toCollectionModel(entities)
                .add(WebMvcLinkBuilder.linkTo(PedidoController.class).withSelfRel());
    }
}
