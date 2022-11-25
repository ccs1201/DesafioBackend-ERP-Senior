package com.ccs.erp.domain.desconto;

import com.ccs.erp.domain.model.entity.ItemPedido;

public class DescontoItem implements Desconto {

    @Override
    public void aplicarDesconto(int percentualDesconto, ItemPedido itemPedido) {

        //Calcula o valor do desconto
        var vlrDesconto = calcularDesconto(percentualDesconto, itemPedido);

        itemPedido.setValorDesconto(vlrDesconto);
    }
}
