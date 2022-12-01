package com.ccs.erp.domain.entity;

import com.ccs.erp.core.exception.DescontoException;
import com.ccs.erp.core.exception.PedidoException;
import com.ccs.erp.domain.factory.ItemFactory;
import com.ccs.erp.domain.factory.ItemPedidoFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class PedidoTest {
    private static final int PERCENTUAL_DESCONTO_ZERO = 0;
    private static final int PERCENTUAL_DESCONTO_NEGATIVO = -1;
    private static final int PERCENTUAL_DESCONTO_MAIOR_MAXIMO = 101;
    private static final int PERCENTUAL_DESCONTO_CEM = 100;
    private final int PERCENTUAL_DESCONTO_DEZ = 10;
    private Item itemProduto;
    private Item itemServico;
    private ItemPedido itemPedidoProduto;
    private ItemPedido itemPedidoServico;
    private Pedido pedido;

    @BeforeEach
    void setup() {

        this.itemProduto = ItemFactory.buildItemProduto();

        this.itemServico = ItemFactory.buildItemServico();

        this.itemPedidoProduto = ItemPedidoFactory.apenasProduto();

        this.itemPedidoServico = ItemPedidoFactory.apenasServico();


        this.pedido = Pedido.builder()
                .observacao("Teste TESTE Teste")
                .valorTotalDesconto(BigDecimal.ZERO)
                .valorTotalPedido(BigDecimal.ZERO)
                .valorTotalItens(BigDecimal.ZERO)
                .statusPedido(StatusPedido.ABERTO)
                .itensPedido(new ArrayList<>())
                .build();

        pedido.getItensPedido().add(itemPedidoServico);
        pedido.getItensPedido().add(itemPedidoProduto);
    }

    @Test
    @DisplayName("Testa Total Pedido sem desconto")
    void testaTotalPedidoSemDesconto() {
        var totalPedidoExpected = BigDecimal.valueOf(2000).setScale(2);

        assertEquals(totalPedidoExpected, pedido.getValorTotalPedido());
    }

    @Test
    @DisplayName("Testa total pedido com desconto")
    void testaTotalPedidoComDesconto() {
        //Garante q o valor total esteja calculado.
        pedido.getValorTotalPedido();

        var totalExpected = BigDecimal.valueOf(1900).setScale(2);

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ);

        assertEquals(totalExpected, pedido.getValorTotalPedido());
    }

    @Test
    @DisplayName("Testa desconto 100 por cento")
    void testaDescontoCem() {

        var totalDescontoExpected = BigDecimal.valueOf(1000).setScale(2);

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_CEM);

        assertEquals(totalDescontoExpected, pedido.getValorTotalDesconto());

    }


    @Test
    @DisplayName("Testa Desconto Produto")
    void testaDescontoProduto() {
        var totalDescontoExpected = BigDecimal.valueOf(100).setScale(2);

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ);

        assertEquals(totalDescontoExpected, pedido.getValorTotalDesconto());

    }

    @Test
    @DisplayName("Testa Desconto Negativo")
    void testaDescontoNegativo() {

        assertThrows(DescontoException.class,
                () -> pedido.aplicarDesconto(PERCENTUAL_DESCONTO_NEGATIVO));
    }

    @Test
    @DisplayName("Testa Desconto Maior que 100")
    void testaDescontoMaiorQueCem() {

        assertThrows(DescontoException.class,
                () -> pedido.aplicarDesconto(PERCENTUAL_DESCONTO_MAIOR_MAXIMO));
    }

    @Test
    @DisplayName("Testa valor total itens sem desconto")
    void testaValorTotalItensSemDesconto() {
        var totalItensExpected = BigDecimal.valueOf(2000).setScale(2);

        assertEquals(totalItensExpected, pedido.getValorTotalItens());
    }

    @Test
    @DisplayName("Testa valor total itens com desconto")
    void testaValorTotalItensComDesconto() {
        pedido.getValorTotalItens();

        var totalItensExpected = BigDecimal.valueOf(2000).setScale(2);

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ);

        assertEquals(totalItensExpected, pedido.getValorTotalItens());
    }

    @Test
    @DisplayName("Testa aplicar desconto com pedido fechado")
    void testaAplicarDescontoPedidoFechado() {
        pedido.fechar();

        assertThrows(DescontoException.class,
                () ->
                        pedido.aplicarDesconto(1));
    }


    @Test
    @DisplayName("Testa aplicar desconto em item servico")
    void aplicarDescontoServico() {
        var pedido = Pedido.builder()
                .observacao("Teste TESTE Teste")
                .valorTotalDesconto(BigDecimal.ZERO)
                .valorTotalPedido(BigDecimal.ZERO)
                .valorTotalItens(BigDecimal.ZERO)
                .statusPedido(StatusPedido.ABERTO)
                .itensPedido(new LinkedList<>())
                .build();
        pedido.addItemPedido(itemPedidoServico);

        var totalExpected = BigDecimal.valueOf(1000).setScale(2);

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ);

        assertEquals(totalExpected, pedido.getValorTotalPedido());

    }

    @Test
    @DisplayName("Testa Aplicar Desconto duas vezes")
    void testaAplicarDescontoDuasVezes() {

        var segundoResultado = BigDecimal.valueOf(1000).setScale(2);
        pedido.getValorTotalPedido();

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ);

        pedido.aplicarDesconto(100);

        assertEquals(segundoResultado, pedido.getValorTotalPedido());

    }

    @Test
    @DisplayName("Testa total após adicionar item")
    void totalAposAdicinarItem() {

        var totalDesconto = BigDecimal.valueOf(200).setScale(2);
        var totalPedido = BigDecimal.valueOf(2800).setScale(2);
        var totalItens = BigDecimal.valueOf(3000).setScale(2);


        pedido.setPercentualDesconto(PERCENTUAL_DESCONTO_DEZ);
        pedido.addItemPedido(ItemPedidoFactory.apenasProduto());

        assertEquals(totalDesconto, pedido.getValorTotalDesconto());
        assertEquals(totalPedido, pedido.getValorTotalPedido());
        assertEquals(totalItens, pedido.getValorTotalItens());

    }

    @Test
    @DisplayName("Testa total após remover item")
    void totalAposRemoverItem() {

        var totalDesconto = BigDecimal.valueOf(100).setScale(2);
        var totalPedido = BigDecimal.valueOf(900).setScale(2);
        var totalItens = BigDecimal.valueOf(1000).setScale(2);


        pedido.setPercentualDesconto(PERCENTUAL_DESCONTO_DEZ);
        pedido.removerItemPedido(pedido.getItensPedido().stream().findFirst().get().getId());

        assertEquals(totalDesconto, pedido.getValorTotalDesconto());
        assertEquals(totalPedido, pedido.getValorTotalPedido());
        assertEquals(totalItens, pedido.getValorTotalItens());

    }

    @Test
    @DisplayName("Testa adicionar Item ao pedido")
    void testaAdicionarItemAoPedido() {
        pedido.addItemPedido(ItemPedidoFactory.apenasProduto());

        assertEquals(3, pedido.getItensPedido().size());
    }

    @Test
    @DisplayName("Testa adicionar Item ao pedido com item inativo")
    void testaAdicionarItemInativoAoPedido() {

        assertThrows(PedidoException.class, () ->
                pedido.addItemPedido(ItemPedidoFactory.itemProdutoInativo())
        );
    }

    @Test
    @DisplayName("Testa adicionar Item com pedido FECHADO")
    void testaAdicionarItemComPedidoFechado() {
        assertThrows(PedidoException.class, () -> {

            pedido.setStatusPedido(StatusPedido.FECHADO);
            pedido.addItemPedido(ItemPedidoFactory.apenasProduto());
        });
    }

    @Test
    @DisplayName("Testa Remover Item com Pedido FECHADO")
    void testaRemoverItemComPedidoFechado() {

        assertThrows(PedidoException.class, () -> {

            pedido.setStatusPedido(StatusPedido.FECHADO);
            pedido.removerItemPedido(ItemPedidoFactory.apenasProduto().getId());
        });
    }

    @Test
    @DisplayName("Testa Remover Item do Pedido")
    void testaRemoverItemDoPedido() {
        pedido.removerItemPedido(pedido.getItensPedido().stream().findAny().get().getId());
        assertEquals(1, pedido.getItensPedido().size());
    }

    @Test
    @DisplayName("Testa Remover Item do Pedido com ID inexistente")
    void testaRemoverItemComIdInexistente() {
        assertThrows(PedidoException.class, () ->
                pedido.removerItemPedido(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Verifica total Pedido após remover item")
    void verificarTotalAposRemoverItem() {
        var totalExpected = BigDecimal.valueOf(1000).setScale(2);
        pedido.removerItemPedido(pedido.getItensPedido().stream().findAny().get().getId());
        assertEquals(totalExpected, pedido.getValorTotalPedido());
    }

    @Test
    @DisplayName("Verifica total Pedido após Adicionar item")
    void verificarTotalAposAdicionarItem() {
        var totalExpected = BigDecimal.valueOf(3000).setScale(2);

        pedido.addItemPedido(ItemPedidoFactory.apenasProduto());

        assertEquals(totalExpected, pedido.getValorTotalPedido());
    }


}