package com.ccs.erp.domain.factory;

import com.ccs.erp.domain.entity.Item;
import com.ccs.erp.domain.entity.ItemPedido;
import com.ccs.erp.domain.entity.Pedido;
import com.ccs.erp.domain.repository.ItemRepository;


public class ItemPedidoFactory {

    private static final int QTD_DEZ = 10;

    /**
     * Constrói um {@link ItemPedido} com apenas
     * Itens do tipo SERVICO e a QTD de 10(DEZ)
     *
     * @return {@link ItemPedido}
     */
    public static ItemPedido apenasServico() {
        var itemPedido = ItemPedido.builder()
                .quantidade(QTD_DEZ)
                .build();

        itemPedido.setItem(ItemFactory.buildItemServico());

        return itemPedido;
    }

    /**
     * Constrói um {@link ItemPedido} com apenas
     * Itens do tipo PRODUTO e a QTD de 10(DEZ)
     *
     * @return {@link ItemPedido}
     */
    public static ItemPedido apenasProduto() {
        var itemPedido = ItemPedido.builder()
                .quantidade(QTD_DEZ)
                .build();
        itemPedido.setItem(ItemFactory.buildItemProduto());

        return itemPedido;
    }

    /**
     * Constrói um {@link ItemPedido} com apenas
     * {@link Item} vindo do banco e QTD de 10(DEZ)
     *
     * @return {@link ItemPedido}
     */
    public static ItemPedido comItemFromBD(Pedido pedido, ItemRepository itemRepository) {
        var itemPedido = ItemPedido.builder()
                .pedido(pedido)
                .quantidade(QTD_DEZ)
                .build();
        itemPedido.setItem(itemRepository.findAll().get(0));

        return itemPedido;
    }
}
