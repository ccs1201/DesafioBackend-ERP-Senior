package com.ccs.erp.core.utils.mapper;

import com.ccs.erp.api.model.input.ItemPedidoInput;
import com.ccs.erp.api.model.response.ItemPedidoResponse;
import com.ccs.erp.api.v1.controller.ItemPedidoController;
import com.ccs.erp.core.utils.hateoas.LinksBuilder;
import com.ccs.erp.domain.entity.ItemPedido;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class ItemPedidoMapper extends AbstractMapper<ItemPedidoResponse, ItemPedidoInput, ItemPedido> {

    private final LinksBuilder linksBuilder;

    public ItemPedidoMapper(LinksBuilder linksBuilder) {
        super(ItemPedidoController.class, ItemPedidoResponse.class);
        this.linksBuilder = linksBuilder;
    }

    @Override
    public ItemPedidoResponse toModel(ItemPedido itemPedido) {
        var response = super.toModel(itemPedido);
        linksBuilder.linkToItemPedidoResponse(response, itemPedido.getPedido().getId());
        return response;
    }

    @Override
    public CollectionModel<ItemPedidoResponse> toCollectionModel(Iterable<? extends ItemPedido> entities) {
        return super.toCollectionModel(entities)
                .add(WebMvcLinkBuilder.linkTo(ItemPedidoController.class).withSelfRel());
    }
}
