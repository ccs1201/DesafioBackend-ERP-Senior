package com.ccs.erp.core.utils.hateoas;

import com.ccs.erp.api.model.response.ItemPedidoResponse;
import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.api.model.response.PedidoResponse;
import com.ccs.erp.api.v1.controller.ItemController;
import com.ccs.erp.api.v1.controller.ItemPedidoController;
import com.ccs.erp.api.v1.controller.PedidoController;
import com.ccs.erp.domain.entity.StatusPedido;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
            linkToItemPedidoResponse(itemPedidoResponse, response.getId());
            this.linkToItemResponse(itemPedidoResponse.getItem());
        });

        response
                .add(linkTo(methodOn(pedidoControllerClass).getById(response.getId())).withSelfRel())
                .add(linkTo(methodOn(pedidoControllerClass).atualizar(null, response.getId())).withRel("atualizar"))
                .add(linkTo(methodOn(pedidoControllerClass).excluir(response.getId())).withRel("excluir"));
        //Adiciona os links conforme o status do Pedido
        response.add((response.getStatusPedido().equals(StatusPedido.FECHADO)) ?
                        linkTo(methodOn(pedidoControllerClass).abrir(response.getId())).withRel("abrir") :
                        linkTo(methodOn(pedidoControllerClass).fechar(response.getId())).withRel("fechar"))
                .add(linkTo(methodOn(pedidoControllerClass)
                        .adicionarItem(response.getId(), null)).withRel("adicionar item"))
                .add(linkTo(methodOn(pedidoControllerClass)
                        .aplicarDescontoSomenteProduto(response.getId(),null)).withRel("aplicar desconto"))
                .add(linkTo(pedidoControllerClass).withRel("pedidos"));
    }

    public void linkToItemPedidoResponse(ItemPedidoResponse response, UUID idPedido) {

        response
                .add(linkTo(methodOn(itemPedidoControllerClass).findById(response.getId())).withSelfRel())
                .add(linkTo(methodOn(pedidoControllerClass)
                        .removerItem(idPedido, response.getId())).withRel("remover item"))
                .add(linkTo(itemPedidoControllerClass).withRel("itensPedido"));

    }
}
