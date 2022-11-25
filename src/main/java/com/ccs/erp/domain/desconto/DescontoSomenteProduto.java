package com.ccs.erp.domain.desconto;

import com.ccs.erp.domain.model.entity.ItemPedido;
import com.ccs.erp.domain.model.entity.TipoItem;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DescontoSomenteProduto implements Desconto {

    @Override
    public void aplicarDesconto(int percentualDesconto, ItemPedido itemPedido) {

        //Se o item for um Produto aplica o desconto
        if (itemPedido.getItem().getTipoItem().equals(TipoItem.PRODUTO)) {

            //Calcula o valor do desconto
            var vlrDesconto = calcularDesconto(percentualDesconto, itemPedido);

            itemPedido.setValorDesconto(vlrDesconto);
        }
    }
}
