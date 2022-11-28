package com.ccs.erp.domain.entity;

import com.ccs.erp.infrastructure.exception.ValorItemNegativoException;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private BigDecimal valorUnitario;

    @Column(nullable = false)
    private BigDecimal valorTotalItem;

    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal valorDesconto;

    @Positive
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    /**
     * <p>Garante que ao setar um item
     * seu valor seja setado em {@code valorUnitario} </p>
     */
    public void setItem(Item item) {
        this.item = item;
        this.valorUnitario = item.getValor();
    }

    /**
     * <p>Garante que {@code valorDesconto} seja definido como
     * ZERO, caso não tenha desconto, para garantir a integridade do banco</p>
     *
     * @param valorDesconto
     */
    public void setValorDesconto(BigDecimal valorDesconto) {
        if (valorDesconto == null) {
            this.valorDesconto = BigDecimal.ZERO;
        }
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorUnitario() {
        if (valorUnitario == null) {
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

        valorTotalItem = valorUnitario.multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);

    }

    /**
     * <p>Cacula o valor total do desconto</p>
     * <br>
     * <p>{@code valorDesconto} * {@code qtd}</p>
     */
    private void calcularTotalDesconto() {

        valorDesconto = valorDesconto
                .multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);
    }

}