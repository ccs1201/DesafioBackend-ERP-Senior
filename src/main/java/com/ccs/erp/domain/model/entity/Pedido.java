package com.ccs.erp.domain.model.entity;

import com.ccs.erp.domain.desconto.Desconto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pedido")
    private Collection<ItemPedido> itensPedido;
    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal valorTotalItens;

    private BigDecimal valorTotalDesconto;

    private BigDecimal valorTotalPedido;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    private int percentualDesconto;


    private String observacao;


    public void addItemPedido(ItemPedido itemPedido) {
        itemPedido.setPedido(this);
        itensPedido.add(itemPedido);
    }

    /**
     * <p>Aplica um desconto percentual aos Itens do Pedido</p>
     * <br>
     *
     * @param percentualDesconto Inteiro que representa o desconto a ser aplicado.
     * @param desconto           Interface que represent ao tipo de desconto a ser aplicado.
     */
    public void aplicarDesconto(int percentualDesconto, Desconto desconto) {
        this.percentualDesconto = percentualDesconto;

        itensPedido.forEach((itemPedido ->
                desconto.aplicarDesconto(percentualDesconto, itemPedido)

        ));
    }

    /**
     * <p>Calcula os totais para {@code totalPedido} e
     * {@code totalDesconto} antes de persistir no banco</p>
     */
    @PrePersist
    public void calcularTotaisPedido() {

        itensPedido.forEach(itemPedido -> {
            //Calcula o total do Pedido
            adicionaValorTotalItensPedido(itemPedido);

            //Calcula o Total Desconto
            adicionaDescontoItem(itemPedido);
        });

        //calcula o valor do pedido
        calcularValorFinalPedido();
    }

    /**
     * <p>Calculo o valor total do pedido</p>
     * <br>
     * <p> {@code valorTotalItens} - {@code valorTotalDesconto}</p>
     */
    private void calcularValorFinalPedido() {
        valorTotalPedido = valorTotalItens
                .subtract(valorTotalDesconto)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * <p>Adiciona o valor total do desconto dos itens
     * ao {@code valorTotalDesconto} do pedido</p>
     *
     * @param itemPedido
     */
    private void adicionaDescontoItem(ItemPedido itemPedido) {
        valorTotalDesconto = valorTotalDesconto
                .add(itemPedido.getValorDesconto())
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * <p>Adiciona o valor total dos itens
     * ao {@code valorTotalItens} do pedido</p>
     *
     * @param itemPedido
     */
    private void adicionaValorTotalItensPedido(ItemPedido itemPedido) {

        valorTotalItens = valorTotalItens
                .add(itemPedido.getValorTotalItem())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
