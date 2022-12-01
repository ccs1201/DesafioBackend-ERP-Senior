package com.ccs.erp.domain.entity;

import com.ccs.erp.core.exception.DescontoException;
import com.ccs.erp.core.exception.PedidoException;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
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
    private BigDecimal valorTotalItens = BigDecimal.ZERO;
    @PositiveOrZero
    private BigDecimal valorTotalDesconto = BigDecimal.ZERO;
    @PositiveOrZero
    private BigDecimal valorTotalPedido = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido statusPedido;
    @PositiveOrZero
    private int percentualDesconto;

    @Column(length = 150)
    private String observacao;

    @CreationTimestamp
    private OffsetDateTime dataPedido;

    @UpdateTimestamp
    private OffsetDateTime ultimaAtualizacao;

    @Transient
    private final String MSG_PEDIDO_FECHADO = "Pedido FECHADO, não pode ser alterado.";

    public BigDecimal getValorTotalPedido() {
        if (valorTotalPedido.equals(BigDecimal.ZERO)) {
            this.calcularTotais();
        }
        return valorTotalPedido;
    }

    public BigDecimal getValorTotalItens() {
        if (valorTotalItens.equals(BigDecimal.ZERO)) {
            this.calcularTotais();
        }
        return valorTotalItens;
    }

    /**
     * <p>Adiciona um {@link ItemPedido} ao pedido.</p>
     * <p>Somenete se o {@link Item} estiver ativo e
     * statusPedido estiver ABERTO e atualiza os totais</p>
     *
     * @param itemPedido Item a ser adicionado.
     * @throws PedidoException se o Item estiver inativo ou o Pedido FECHADO
     */
    public void addItemPedido(ItemPedido itemPedido) {

        if (statusPedido.equals(StatusPedido.FECHADO)) {
            throw new PedidoException(MSG_PEDIDO_FECHADO);
        }

        if (!itemPedido.getItem().getAtivo()) {
            throw new PedidoException(String
                    .format("Item: %s inativo, não pode ser adicionado ao pedido.", itemPedido.getItem().getNome()));
        }

        if (itensPedido == null) {
            itensPedido = new ArrayList<>();
        }
        this.itensPedido.add(itemPedido);
        calcularTotais();
    }


    public void aplicarDesconto(int percentualDesconto) {

        this.podeAplicarDesconto();
        this.validarLimiteDesconto(percentualDesconto);

        //Garante que o desconto esteja zerado para evitar o acumulo
        //qdo o desconto for aplicado várias vezes
        this.valorTotalDesconto = BigDecimal.ZERO;

        this.percentualDesconto = percentualDesconto;

        this.itensPedido.forEach(itemPedido -> {
            if (itemPedido.getItem().getTipoItem() == TipoItem.PRODUTO) {
                this.calcularDesconto(itemPedido);
            }
        });
    }

    private void calcularDesconto(ItemPedido itemPedido) {

        //Percentual do desconto em decimal.
        // Ex. 10/100 = 0,1
        var descontoDecimal = descontoToDecimal();

        //Calcula o desconto unitário.
        //Ex. 100*0,1 = 10
        var vlrDescontoUnitario = itemPedido.getValorUnitario().multiply(descontoDecimal).setScale(2, RoundingMode.HALF_UP);

        //Calcula o desconto total do item.
        //Ex. 10*5 = 50
        var vlrDescontoTotalItem = vlrDescontoUnitario.multiply(BigDecimal.valueOf(itemPedido.getQuantidade()).setScale(2, RoundingMode.HALF_UP));


        this.atualizarValorTotalDesconto(vlrDescontoTotalItem);

        this.atualizarValorTotalPedido();
    }

    /**
     * <p>Atualiza o valor total do pedido subtraindo
     * do {@code valorTotalItens} o valor de
     * {@code valorTotalDesconto}</p>
     * <br>
     * <p> {@code valorTotalItens} - {@code valorTotalDesconto}</p>
     */
    private void atualizarValorTotalPedido() {
        valorTotalPedido = valorTotalItens.subtract(valorTotalDesconto).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Atualiza o {@code valorTotalDesconto}
     *
     * @param vlrDescontoTotalItem
     */
    private void atualizarValorTotalDesconto(BigDecimal vlrDescontoTotalItem) {
        valorTotalDesconto = valorTotalDesconto.add(vlrDescontoTotalItem).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal descontoToDecimal() {

        float percentualDecimal = percentualDesconto / 100F;

        return BigDecimal.valueOf(percentualDecimal).setScale(3, RoundingMode.HALF_UP);
    }

    private void validarLimiteDesconto(int percentualDesconto) {

        if (percentualDesconto < 1 || percentualDesconto > 100) {
            throw new DescontoException("Percentual de Desconto não permito, informe um valor entre 1 e 100.");
        }
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
     * <p>Calcula os totais para {@code valorTotalPedido}, {@code valorTotalItens} </p>
     */
    public void calcularTotais() {

        valorTotalItens = BigDecimal.ZERO;
        valorTotalPedido = BigDecimal.ZERO;

        //Soma o total dos itens
        itensPedido.forEach((itemPedido -> {
            itemPedido.setPedido(this);
            valorTotalItens = valorTotalItens.add(itemPedido.getValorTotalItem());
        }));

        //calcula o valor do pedido
        atualizarValorTotalPedido();
    }

    /**
     * Seta o {@link StatusPedido} do pedido
     * como {@code  StatusPedido.FECHADO}
     */
    public void fechar() {
        statusPedido = StatusPedido.FECHADO;
    }

    /**
     * Seta o {@link StatusPedido} do pedido como {@code StatusPedido.ABERTO}
     */
    public void abrir() {
        statusPedido = StatusPedido.ABERTO;
    }

    /**
     * <p>Remove um {@link ItemPedido} do pedido somente
     * se o Pedido estiver ABERTO e atualiza os totais</p>
     *
     * @param idItemPedido id do {@link ItemPedido} a ser removido
     * @throws PedidoException se o pedido estiver fechado ou
     *                         se o ItemPedido não existir
     */
    public void removerItemPedido(UUID idItemPedido) {
        if (statusPedido.equals(StatusPedido.FECHADO)) {
            throw new PedidoException(MSG_PEDIDO_FECHADO);
        }

        //Aqui buscamos o itemPedido diretamente no pedido em memória em tese mais rápido
        //outra aopção seria buscarmos o itemPedido diretamente no banco pelo ID.
        //é uma decissão de design :)
        var itemPedido = getItensPedido()
                .stream()
                .filter(itemPedido1 -> itemPedido1.getId().equals(idItemPedido))
                .findFirst()
                .orElseThrow(() ->
                        new PedidoException("O item ID: %s, não pertence ao pedido informado."));

        itensPedido.remove(itemPedido);

        calcularTotais();
    }
}
