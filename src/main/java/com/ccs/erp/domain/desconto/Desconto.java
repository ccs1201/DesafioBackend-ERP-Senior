package com.ccs.erp.domain.desconto;

import com.ccs.erp.domain.entity.ItemPedido;
import com.ccs.erp.infrastructure.exception.DescontoPercentualNaoPermitidoException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface Desconto {

    void aplicarDesconto(int percentualDesconto, ItemPedido itemPedido);


    /**
     * <p>verifica se o percentual do Desconto esta
     * entre o limites permitidos.</p>
     *
     * <p>Os limites são: inferior é 1 e o superior 100</p>
     * <br>
     *
     * @throws DescontoPercentualNaoPermitidoException se um dos limiter for ultrapassado.
     */
    private void verificarLimitesDesconto(int percentualDesconto) {
        if (percentualDesconto > 100 || percentualDesconto < 1) {
            throw new DescontoPercentualNaoPermitidoException(
                    String.format("O desconto de %d, não é permitido, entre com um valor entre 1 e 100.", percentualDesconto)
            );
        }
    }

    /**
     * <p>Converte um {@code int} que representa um desconto
     * em Decimal(com casas decimais).</p>
     *
     * <p>
     * Ex. <br>
     * {@code percentualDesconto} = 15, significa que queremos
     * aplicar um desconto de 15% <br>
     * Então 15/100 = 0.15 <br>
     * Retorna 0.15
     * </p>
     *
     * @param percentualDesconto Um inteiro que representa o percentual de desconto a ser aplicado.
     * @return {@link Float} representando o desconto
     */
    private Float descontoToDecimal(int percentualDesconto) {

        return (percentualDesconto / 100f);
    }

    /**
     * <p>Calcula o valor do desconto para o item</p>
     * <br>
     * @param percentualDesconto O desconto que será aplicado (percentual)
     * @param itemPedido O item em que será aplicado o desconto.
     * @return O valor do desconto.
     */
    default BigDecimal calcularDesconto(int percentualDesconto, ItemPedido itemPedido) {

        verificarLimitesDesconto(percentualDesconto);

        var descontoDecimal = descontoToDecimal(percentualDesconto);

        var vlrDesconto = itemPedido.getValorUnitario()
                .multiply(BigDecimal.valueOf(descontoDecimal)
                //Seta 2 casas decimais e o arredondamento
        ).setScale(2, RoundingMode.HALF_UP);

        return vlrDesconto;
    }

}