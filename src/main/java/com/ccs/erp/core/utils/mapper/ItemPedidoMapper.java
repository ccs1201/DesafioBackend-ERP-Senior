package com.ccs.erp.core.utils.mapper;

import com.ccs.erp.api.model.input.ItemPedidoInput;
import com.ccs.erp.api.model.response.ItemPedidoResponse;
import com.ccs.erp.api.v1.controller.PedidoController;
import com.ccs.erp.core.utils.hateoas.LinksBuilder;
import com.ccs.erp.domain.entity.ItemPedido;
import org.springframework.stereotype.Component;

@Component
public class ItemPedidoMapper extends AbstractMapper<ItemPedidoResponse, ItemPedidoInput, ItemPedido> {

    public ItemPedidoMapper(LinksBuilder linksBuilder) {
        super(PedidoController.class, ItemPedidoResponse.class);
    }
}
