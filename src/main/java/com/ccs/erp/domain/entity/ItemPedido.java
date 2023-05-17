package com.ccs.erp.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private BigDecimal valorUnitario;

    @Column(nullable = false)
    private BigDecimal valorTotalItem;

    @Positive
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    /**
     * Garante que ao setar um item
     * seu valor seja setado em {@code valorUnitario}
     * @param item Item que sera adicionado ao pedido
     */
    public void setItem(Item item) {
        this.item = item;
        valorUnitario = item.getValor();
    }

    public BigDecimal getValorUnitario() {
        /*
        garante que o valor unitário receba
        o valor do item
        */
        if (this.valorUnitario == null) {
            this.valorUnitario = item.getValor();
        }
        return valorUnitario;
    }

    public BigDecimal getValorTotalItem() {
        /*
        Garante que o valor total do item
        esteja calculado
        */
        if (valorTotalItem == null) {
            this.calcularTotalItem();
        }
        return valorTotalItem;
    }

    /**
     * <p>Calcula o valor total do Item</p>
     */
    private void calcularTotalItem() {

        //chama o get para garantir que o valor unitario seja setado
        getValorUnitario();

        this.valorTotalItem = valorUnitario.multiply(
                BigDecimal.valueOf(quantidade)
        ).setScale(2, RoundingMode.HALF_UP);

    }
}