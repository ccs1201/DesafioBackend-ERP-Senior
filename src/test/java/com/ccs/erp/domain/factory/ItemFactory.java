package com.ccs.erp.domain.factory;

import com.ccs.erp.domain.entity.Item;
import com.ccs.erp.domain.entity.TipoItem;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemFactory {


    private static final BigDecimal VALOR_ITEM_CEM = BigDecimal.valueOf(100);

    /**
     * Constrói um Item do tipo PRODUTO
     * com VALOR de 100(CEM)
     *
     * @return {@link Item}
     */
    public static Item buildItemProduto() {
        return Item.builder()
                .id(UUID.randomUUID())
                .tipoItem(TipoItem.PRODUTO)
                .valor(VALOR_ITEM_CEM)
                .ativo(true)
                .nome("Item Produto")
                .build();
    }

    /**
     * Constrói um Item do tipo SERVICO
     * com VALOR de 100(CEM)
     *
     * @return {@link Item}
     */
    public static Item buildItemServico() {
        return Item.builder()
                .id(UUID.randomUUID())
                .tipoItem(TipoItem.SERVICO)
                .valor(VALOR_ITEM_CEM)
                .ativo(true)
                .nome("Item Serviço")
                .build();
    }

    public static Item itemProdutoInativo() {
        return Item.builder()
                .id(UUID.randomUUID())
                .tipoItem(TipoItem.PRODUTO)
                .valor(VALOR_ITEM_CEM)
                .ativo(false)
                .nome("Item Produto Inativo")
                .build();
    }

    public static Item itemServicoInativo() {
        return Item.builder()
                .id(UUID.randomUUID())
                .tipoItem(TipoItem.SERVICO)
                .valor(VALOR_ITEM_CEM)
                .ativo(false)
                .nome("Item Servico Inativo")
                .build();
    }
}
