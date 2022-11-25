package com.ccs.erp.domain.entity;

import com.ccs.erp.domain.desconto.DescontoItem;
import com.ccs.erp.domain.desconto.DescontoSomenteProduto;
import com.ccs.erp.domain.desconto.DescontoSomenteServico;
import com.ccs.erp.infrastructure.exception.ValorDescontoNaoPermitidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoTest {

    private static final int PERCENTUAL_DESCONTO_ZERO = 0;

    private static final int PERCENTUAL_DESCONTO_NEGATIVO = -1;

    private static final int PERCENTUAL_DESCONTO_MAIOR_MAXIMO = 101;

    private static final int PERCENTUAL_DESCONTO_CEM = 100;

    private final int PERCENTUAL_DESCONTO_DEZ = 10;

    private final BigDecimal VALOR_ITEM = BigDecimal.valueOf(100);

    private final int QTD = 10;

    private Item itemProduto;

    private Item itemServico;

    private ItemPedido itemPedidoProduto;

    private ItemPedido itemPedidoServico;

    private Pedido pedido;

    @BeforeEach
    void setup() {

        this.itemProduto = Item.builder()
                .tipoItem(TipoItem.PRODUTO)
                .valor(VALOR_ITEM)
                .ativo(true)
                .nome("Item Produto")
                .build();

        this.itemServico = Item.builder()
                .tipoItem(TipoItem.SERVIÇO)
                .valor(VALOR_ITEM)
                .ativo(true)
                .nome("Item Serviço")
                .build();

        this.itemPedidoProduto = ItemPedido.builder()
                .item(itemProduto)
                .valorDesconto(BigDecimal.ZERO)
                .qtd(QTD)
                .build();

        this.itemPedidoServico = ItemPedido.builder()
                .item(itemServico)
                .valorDesconto(BigDecimal.ZERO)
                .qtd(QTD)
                .build();

        this.pedido = Pedido.builder()
                .observacao("Teste TESTE Teste")
                .itensPedido(new ArrayList<>())
                .valorTotalDesconto(BigDecimal.ZERO)
                .valorTotalPedido(BigDecimal.ZERO)
                .valorTotalItens(BigDecimal.ZERO)
                .statusPedido(StatusPedido.ABERTO)
                .build();
        this.pedido.addItemPedido(itemPedidoProduto);
        this.pedido.addItemPedido(itemPedidoServico);

    }

    @Test
    @DisplayName("Testa Total Pedido sem desconto")
    void testaTotalPedidoSemDesconto() {
        BigDecimal totalPedidoExpected = BigDecimal.valueOf(2000).setScale(2);

        pedido.calcularTotaisPedido();

//        assertTrue(totalPedidoExpected.compareTo(pedido.getValorTotalPedido()) == 0);
        assertEquals(totalPedidoExpected, pedido.getValorTotalPedido());
    }

    @Test
    @DisplayName("Testa desconto em todos o itens")
    void testaDescontoTodos() {

        BigDecimal totalDescontoExpected = BigDecimal.valueOf(200).setScale(2);

        var desconto = new DescontoItem();

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ, desconto);
        pedido.calcularTotaisPedido();

        assertEquals(totalDescontoExpected, pedido.getValorTotalDesconto());
    }

    @Test
    @DisplayName("Testa Total Pedido com Desconto em todos os itens")
    void testaTotalPedidoComDescontoTodosItens() {

        BigDecimal totalPedidoExpected = BigDecimal.valueOf(1800).setScale(2);

        var desconto = new DescontoItem();

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ, desconto);
        pedido.calcularTotaisPedido();

        assertEquals(totalPedidoExpected, pedido.getValorTotalPedido());
    }

    @Test
    @DisplayName("Testa Total Pedido com Desconto Somente Produtos")
    void testaTotalPedidoComDescontoSomenteProduto() {

        BigDecimal totalPedidoExpected = BigDecimal.valueOf(1900).setScale(2);

        var desconto = new DescontoSomenteProduto();

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ, desconto);
        pedido.calcularTotaisPedido();

        assertEquals(totalPedidoExpected, pedido.getValorTotalPedido());
    }

    @Test
    @DisplayName("Testa Total Pedido com Desconto somente Servicos")
    void testaTotalPedidoComDescontoSomenteServico() {

        BigDecimal totalPedidoExpected = BigDecimal.valueOf(1900).setScale(2);

        var desconto = new DescontoSomenteServico();

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ, desconto);
        pedido.calcularTotaisPedido();

        assertEquals(totalPedidoExpected, pedido.getValorTotalPedido());
    }

    @Test
    @DisplayName("Testa desconto CEM porcento")
    void testaDescontoCem() {

        BigDecimal totalDescontoExpected = BigDecimal.valueOf(2000).setScale(2);

        var desconto = new DescontoItem();

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_CEM, desconto);
        pedido.calcularTotaisPedido();

        assertEquals(totalDescontoExpected, pedido.getValorTotalDesconto());
    }

    @Test
    @DisplayName("Testa Desconto Produto")
    void testaDescontoProduto() {
        BigDecimal totalDescontoExpected = BigDecimal.valueOf(100).setScale(2);

        var desconto = new DescontoSomenteProduto();

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ, desconto);
        pedido.calcularTotaisPedido();

        assertEquals(totalDescontoExpected, pedido.getValorTotalDesconto());

    }

    @Test
    @DisplayName("Testa Desconto Servico")
    void testaDescontoServico() {
        BigDecimal totalDescontoExpected = BigDecimal.valueOf(100).setScale(2);

        var desconto = new DescontoSomenteServico();

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ, desconto);
        pedido.calcularTotaisPedido();

        assertEquals(totalDescontoExpected, pedido.getValorTotalDesconto());

    }

    @Test
    @DisplayName("Testa Desconto ZERO")
    void testaDescontoZero() {
        BigDecimal totalDescontoExpected = BigDecimal.valueOf(100).setScale(2);

        var desconto = new DescontoItem();
        pedido.calcularTotaisPedido();

        assertThrows(ValorDescontoNaoPermitidoException.class,
                () -> pedido.aplicarDesconto(PERCENTUAL_DESCONTO_ZERO, desconto));
    }

    @Test
    @DisplayName("Testa Desconto Negativo")
    void testaDescontoNegativo() {
        BigDecimal totalDescontoExpected = BigDecimal.valueOf(100).setScale(2);

        var desconto = new DescontoItem();
        pedido.calcularTotaisPedido();

        assertThrows(ValorDescontoNaoPermitidoException.class,
                () -> pedido.aplicarDesconto(PERCENTUAL_DESCONTO_NEGATIVO, desconto));
    }

    @Test
    @DisplayName("Testa Desconto Maior que 100")
    void testaDescontoMaioRQueCem() {
        BigDecimal totalDescontoExpected = BigDecimal.valueOf(100).setScale(2);

        var desconto = new DescontoItem();
        pedido.calcularTotaisPedido();

        assertThrows(ValorDescontoNaoPermitidoException.class,
                () -> pedido.aplicarDesconto(PERCENTUAL_DESCONTO_MAIOR_MAXIMO, desconto));
    }

    @Test
    @DisplayName("Testa valor total itens sem desconto")
    void testaValorTotalItensSemDesconto() {
        BigDecimal totalItensExpected = BigDecimal.valueOf(2000).setScale(2);

        pedido.calcularTotaisPedido();
        assertEquals(totalItensExpected, pedido.getValorTotalItens());
    }

    @Test
    @DisplayName("Testa valor total itens com desconto")
    void testaValorTotalItensComDesconto() {
        BigDecimal totalItensExpected = BigDecimal.valueOf(2000).setScale(2);

        var desconto = new DescontoItem();

        pedido.aplicarDesconto(PERCENTUAL_DESCONTO_DEZ, desconto);
        pedido.calcularTotaisPedido();

        assertEquals(totalItensExpected, pedido.getValorTotalItens());
    }

}