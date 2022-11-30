package com.ccs.erp.domain.entity;

import com.ccs.erp.domain.factory.ItemFactory;
import com.ccs.erp.domain.factory.ItemPedidoFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ItemPedidoTest {

    private final Item itemProduto;
    private final Item itemServico;

    private final ItemPedido itemPedido;

    ItemPedidoTest() {
        this.itemProduto = ItemFactory.buildItemProduto();
        this.itemServico = ItemFactory.buildItemServico();
        this.itemPedido = ItemPedido.builder()
                .item(itemProduto)
                .quantidade(10)
                .build();
    }


    @Test
    @DisplayName("Testa o calculo do total dos itens")
    void calcularTotalItem() {

        var expected = BigDecimal.valueOf(1000).setScale(2);

        Assertions.assertEquals(expected, itemPedido.getValorTotalItem());

    }
}