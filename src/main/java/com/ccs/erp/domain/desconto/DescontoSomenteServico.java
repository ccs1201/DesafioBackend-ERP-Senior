package com.ccs.erp.domain.desconto;

import com.ccs.erp.domain.model.entity.ItemPedido;
import com.ccs.erp.domain.model.entity.TipoItem;

public class DescontoSomenteServico implements Desconto {

    @Override
    public void aplicarDesconto(int percentualDesconto, ItemPedido itemPedido) {

        //Se o item for um SERVIÇO aplica o desconto
        if (itemPedido.getItem().getTipoItem().equals(TipoItem.SERVIÇO)) {

            //Calcula o valor do desconto
            var vlrDesconto = calcularDesconto(percentualDesconto, itemPedido);

            itemPedido.setValorDesconto(vlrDesconto);
        }
    }
}
