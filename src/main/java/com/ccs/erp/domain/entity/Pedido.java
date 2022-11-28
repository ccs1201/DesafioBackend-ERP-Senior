package com.ccs.erp.domain.entity;

import com.ccs.erp.domain.desconto.Desconto;
import com.ccs.erp.infrastructure.exception.DescontoException;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamicUpdate
public class Pedido {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false)
    private UUID id;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pedido", cascade = CascadeType.ALL)
    private Collection<ItemPedido> itensPedido;
    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal valorTotalItens;
    private BigDecimal valorTotalDesconto;
    private BigDecimal valorTotalPedido;
    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;
    private int percentualDesconto;

    @Column(length = 150)
    private String observacao;

    @CreationTimestamp
    private OffsetDateTime dataPedido;

    @UpdateTimestamp
    private OffsetDateTime ultimaAtualizacao;

    /**
     * <p>Adiciona um item ao Pedido</p>
     *
     * @param itemPedido Item a ser adicionado.
     */
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

        this.podeAplicarDesconto();

        this.percentualDesconto = percentualDesconto;

        itensPedido.forEach((itemPedido ->
                desconto.aplicarDesconto(percentualDesconto, itemPedido)
        ));
    }

    /**
     * <p>Verifica se um desconto pode ser aplicado</p>
     * <p>A regra de negocio define que somente pedido
     * com status aberto podem ter desconto. <br>
     * Por tanto se o pedido estiver com status fechado
     * lançamos uma exceção.
     *
     * </p>
     */
    private void podeAplicarDesconto() {
        if (statusPedido.equals(StatusPedido.FECHADO)) {
            throw new DescontoException("Pedido com status fechado, não é possível aplicar desconto.");
        }
    }

    /**
     * <p>Calcula os totais para {@code valorTotalPedido}, {@code valorTotalItens} e
     * {@code valorTotalDesconto} antes de persistir no banco</p>
     */
    @PrePersist
    public void calcularTotaisPedido() {

        valorTotalDesconto = BigDecimal.ZERO;
        valorTotalItens = BigDecimal.ZERO;
        valorTotalPedido = BigDecimal.ZERO;

        itensPedido.forEach(itemPedido -> {
            //Acumula o total do Pedido
            adicionaValorTotalItensPedido(itemPedido);

            if(percentualDesconto > 0){
                //Acumula o Total Desconto
                adicionaDescontoItem(itemPedido);
            }

        });

        //calcula o valor do pedido
        calcularValorTotalPedido();
    }

    /**
     * <p>Calculo o valor total do pedido</p>
     * <br>
     * <p> {@code valorTotalItens} - {@code valorTotalDesconto}</p>
     */
    private void calcularValorTotalPedido() {
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

    public void fechar() {
        statusPedido = StatusPedido.FECHADO;
    }

    public void abrir() {
        statusPedido = StatusPedido.ABERTO;
    }
}
