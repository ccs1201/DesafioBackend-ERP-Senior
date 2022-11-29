package com.ccs.erp.core.utils.hateoas;

import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.api.model.response.PedidoResponse;
import com.ccs.erp.api.v1.controller.ItemController;
import com.ccs.erp.api.v1.controller.PedidoController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LinksBuilder {

    private final Class<ItemController> itemControllerClass = ItemController.class;
    private final Class<PedidoController> pedidoControllerClass = PedidoController.class;


    public void linkToItemResponse(ItemResponse item) {
        item
                .add(linkTo(methodOn(itemControllerClass).getById(item.getId())).withSelfRel())
                .add(linkTo(methodOn(itemControllerClass).atualizar(null, item.getId())).withRel("atualizar"))
                .add(linkTo(methodOn(itemControllerClass).excluir(item.getId())).withRel("excluir"))
                .add(linkTo(methodOn(itemControllerClass).ativar(item.getId())).withRel("ativar"))
                .add(linkTo(methodOn(itemControllerClass).inativar(item.getId())).withRel("inativar"))
                .add(linkTo(itemControllerClass).withRel("itens"));
    }

    public void linkToPedidoResponse(PedidoResponse pedidoResponse) {
        //adiciona os links do item
        pedidoResponse.getItensPedido().forEach((itemPedidoResponse) -> {
            this.linkToItemResponse(itemPedidoResponse.getItem());
        });

        pedidoResponse
                .add(linkTo(methodOn(pedidoControllerClass).getById(pedidoResponse.getId())).withSelfRel())
                .add(linkTo(methodOn(pedidoControllerClass).atualizar(null, pedidoResponse.getId())).withRel("atualizar"))
                .add(linkTo(methodOn(pedidoControllerClass).excluir(pedidoResponse.getId())).withRel("excluir"))
                .add(linkTo(methodOn(pedidoControllerClass).abrir(pedidoResponse.getId())).withRel("abrir"))
                .add(linkTo(methodOn(pedidoControllerClass).fechar(pedidoResponse.getId())).withRel("fechar"));
    }
}
