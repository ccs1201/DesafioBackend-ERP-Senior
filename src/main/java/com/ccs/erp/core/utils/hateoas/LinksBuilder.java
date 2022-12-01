package com.ccs.erp.core.utils.hateoas;

import com.ccs.erp.api.model.response.ItemPedidoResponse;
import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.api.model.response.PedidoResponse;
import com.ccs.erp.api.v1.controller.ItemController;
import com.ccs.erp.api.v1.controller.ItemPedidoController;
import com.ccs.erp.api.v1.controller.PedidoController;
import com.ccs.erp.domain.entity.StatusPedido;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LinksBuilder {

    private final Class<ItemController> itemControllerClass = ItemController.class;
    private final Class<PedidoController> pedidoControllerClass = PedidoController.class;
    private final Class<ItemPedidoController> itemPedidoControllerClass = ItemPedidoController.class;


    public void linkToItemResponse(ItemResponse response) {
        response
                .add(linkTo(methodOn(itemControllerClass).getById(response.getId())).withSelfRel())
                .add(linkTo(methodOn(itemControllerClass).atualizar(null, response.getId())).withRel("atualizar"))
                .add(linkTo(methodOn(itemControllerClass).excluir(response.getId())).withRel("excluir"))
                //Adiciona Ativar Inativar comforme o status do Item
                .add((response.getAtivo()) ?
                        linkTo(methodOn(itemControllerClass).inativar(response.getId())).withRel("inativar") :
                        linkTo(methodOn(itemControllerClass).ativar(response.getId())).withRel("ativar"));

        response.add(linkTo(itemControllerClass).withRel("itens"));
    }

    public void linkToPedidoResponse(PedidoResponse response) {
        //adiciona os links do item
        response.getItensPedido().forEach((itemPedidoResponse) -> {
            linkToItemPedidoResponse(itemPedidoResponse);
            this.linkToItemResponse(itemPedidoResponse.getItem());
        });

        response
                .add(linkTo(methodOn(pedidoControllerClass).getById(response.getId())).withSelfRel())
                .add(linkTo(methodOn(pedidoControllerClass).atualizar(null, response.getId())).withRel("atualizar"))
                .add(linkTo(methodOn(pedidoControllerClass).excluir(response.getId())).withRel("excluir"));
        //Adiciona os links conforme o stats do Pedido
        response.add((response.getStatusPedido().equals(StatusPedido.FECHADO)) ?
                linkTo(methodOn(pedidoControllerClass).abrir(response.getId())).withRel("abrir") :
                linkTo(methodOn(pedidoControllerClass).fechar(response.getId())).withRel("fechar"));
    }

    public void linkToItemPedidoResponse(ItemPedidoResponse response) {

        response.add(linkTo(itemPedidoControllerClass).withRel("itensPedido"));

    }
}
