package com.ccs.erp.domain.entity;

import com.ccs.erp.infrastructure.exception.ValorItemNegativoException;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @Column(nullable = false)
    private BigDecimal valorUnitario;

    private BigDecimal valorTotalItem;

    private BigDecimal valorDesconto;

    private int qtd;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    /**
     * <p>Garante que ao setar um item
     * seu valor seja setado em {@code valorUnitario} </p>
     */
    public void setItem(Item item) {
        this.item = item;
        this.valorUnitario = item.getValor();
    }

    public BigDecimal getValorUnitario() {
        if(valorUnitario == null){
            this.valorUnitario = item.getValor();
        }
        return valorUnitario;
    }

    public BigDecimal getValorTotalItem() {

        if (valorUnitario == null) {
            this.valorUnitario = item.getValor();
        }

        this.calcularValorTotalItem();

        return this.valorTotalItem;
    }

    public BigDecimal getValorDesconto() {
        calcularTotalDesconto();
        return valorDesconto;
    }

    /**
     * <p> Calcula o valor total do item</p>
     * <br>
     * <p>{@code valorUnitario} * {@code qtd}</p>
     */
    private void calcularValorTotalItem() {

        if (valorUnitario.compareTo(BigDecimal.ZERO) != 1) {
            throw new ValorItemNegativoException("O valor do item não pode ser inferior ou igual a ZERO.");
        }

        valorTotalItem = valorUnitario.multiply(BigDecimal.valueOf(qtd))
                .setScale(2, RoundingMode.HALF_UP);

    }

    /**
     * <p>Cacula o valor total do desconto</p>
     * <br>
     * <p>{@code valorDesconto} * {@code qtd}</p>
     */
    private void calcularTotalDesconto() {

        valorDesconto = valorDesconto
                .multiply(BigDecimal.valueOf(qtd))
                .setScale(2, RoundingMode.HALF_UP);
    }

}