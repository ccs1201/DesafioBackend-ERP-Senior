package com.ccs.erp.infrastructure.utils.mapper;

import com.ccs.erp.api.model.input.ItemPedidoInput;
import com.ccs.erp.api.model.response.ItemPedidoResponse;
import com.ccs.erp.domain.entity.ItemPedido;

public class ItemPedidoMapper extends AbstractMapper<ItemPedidoResponse, ItemPedidoInput, ItemPedido>{
    public ItemPedidoMapper() {
        super(null, ItemPedidoResponse.class);
    }
}
