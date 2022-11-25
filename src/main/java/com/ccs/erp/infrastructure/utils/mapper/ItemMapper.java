package com.ccs.erp.infrastructure.utils.mapper;

import com.ccs.erp.api.model.input.ItemInput;
import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.domain.entity.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper extends AbstractMapper<ItemResponse, ItemInput, Item>{
}
