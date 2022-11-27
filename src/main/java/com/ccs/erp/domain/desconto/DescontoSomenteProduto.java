package com.ccs.erp.domain.desconto;

import com.ccs.erp.domain.entity.ItemPedido;
import com.ccs.erp.domain.entity.TipoItem;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class DescontoSomenteProduto implements Desconto {

    @Override
    public void aplicarDesconto(int percentualDesconto, ItemPedido itemPedido) {

        //Se o item for um Produto aplica o desconto
        if (itemPedido.getItem().getTipoItem().equals(TipoItem.PRODUTO)) {

            //Calcula o valor do desconto para o ItemPedido
            calcularDesconto(percentualDesconto, itemPedido);

        } else {
            //se não for aplicado desconto seta o valorDesconto como ZERO
            itemPedido.setValorDesconto(BigDecimal.ZERO);
        }
    }
}
