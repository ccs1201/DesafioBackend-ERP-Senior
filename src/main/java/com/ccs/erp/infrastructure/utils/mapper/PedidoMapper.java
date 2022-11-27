package com.ccs.erp.infrastructure.utils.mapper;

import com.ccs.erp.api.model.input.PedidoInput;
import com.ccs.erp.api.model.response.PedidoResponse;
import com.ccs.erp.api.v1.controller.PedidoController;
import com.ccs.erp.domain.entity.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper extends AbstractMapper<PedidoResponse, PedidoInput, Pedido> {
    public PedidoMapper() {
        super(PedidoController.class, PedidoResponse.class);
    }

}
