package com.ccs.erp.core.utils.mapper;

import com.ccs.erp.api.model.input.ItemInput;
import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.api.v1.controller.ItemController;
import com.ccs.erp.core.utils.hateoas.LinksBuilder;
import com.ccs.erp.domain.entity.Item;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public final class ItemMapper extends AbstractMapper<ItemResponse, ItemInput, Item> {

    private final LinksBuilder linksBuilder;

    public ItemMapper(LinksBuilder linksBuilder) {
        super(ItemController.class, ItemResponse.class);
        this.linksBuilder = linksBuilder;
    }

    @Override
    public ItemResponse toModel(Item item) {
        var itemResponse = super.toModel(item);
        linksBuilder.linkToItemResponse(itemResponse);

        return itemResponse;

    }

    @Override
    public CollectionModel<ItemResponse> toCollectionModel(Iterable<? extends Item> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(ItemController.class).withSelfRel());
    }
}
