package com.ccs.erp.domain.desconto;

import com.ccs.erp.domain.entity.ItemPedido;
import com.ccs.erp.domain.entity.TipoItem;

import java.math.BigDecimal;

public class DescontoSomenteServico implements Desconto {

    @Override
    public void aplicarDesconto(int percentualDesconto, ItemPedido itemPedido) {

        //Se o item for um SERVIÇO aplica o desconto
        if (itemPedido.getItem().getTipoItem().equals(TipoItem.SERVICO)) {

            //Calcula o valor do desconto para o ItemPedido
            calcularDesconto(percentualDesconto, itemPedido);
        } else {
            //se não for aplicado desconto seta o valorDesconto como ZERO
            itemPedido.setValorDesconto(BigDecimal.ZERO);
        }
    }
}
