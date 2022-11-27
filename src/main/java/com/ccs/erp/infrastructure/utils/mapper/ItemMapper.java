package com.ccs.erp.infrastructure.utils.mapper;

import com.ccs.erp.api.model.input.ItemInput;
import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.api.v1.controller.ItemController;
import com.ccs.erp.domain.entity.Item;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public final class ItemMapper extends AbstractMapper<ItemResponse, ItemInput, Item> {
    public ItemMapper() {
        super(ItemController.class, ItemResponse.class);
    }

    @Override
    public ItemResponse toModel(Item item) {
        return super.toModel(item)
                .add(linkTo(methodOn(ItemController.class).getById(item.getId())).withSelfRel())
                .add(linkTo(methodOn(ItemController.class).update(null,item.getId())).withRel("update"))
                .add(linkTo(methodOn(ItemController.class).delete(item.getId())).withRel("delete"))
                .add(linkTo(methodOn(ItemController.class).ativar(item.getId())).withRel("ativar"))
                .add(linkTo(methodOn(ItemController.class).inativar(item.getId())).withRel("inativar"))
                .add(linkTo(ItemController.class).withRel("itens"));
    }

    @Override
    public CollectionModel<ItemResponse> toCollectionModel(Iterable<? extends Item> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(ItemController.class).withSelfRel());
    }
}
