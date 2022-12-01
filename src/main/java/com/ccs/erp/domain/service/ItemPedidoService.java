package com.ccs.erp.domain.service;

import com.ccs.erp.core.exception.RepositoryEntityNotFoundException;
import com.ccs.erp.domain.entity.ItemPedido;
import com.ccs.erp.domain.entity.Pedido;
import com.ccs.erp.domain.repository.itemPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemPedidoService {

    private final itemPedidoRepository repository;
    private final PedidoService pedidoService;
    private final ItemService itemService;

    public Page<ItemPedido> findAll(Pageable pageable) {

        return repository.findAll(pageable);
    }


    public ItemPedido fidById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new RepositoryEntityNotFoundException(String
                        .format("Item Pedido com id: %s, não existe.", id)));
    }

    public Pedido removerItemDoPedido(UUID IdPedido, UUID idItemPedido) {
        return pedidoService.removerItemPedido(IdPedido, idItemPedido);
    }

    public Pedido cadastrarNovoItemNoPedido(ItemPedido itemPedido, UUID pedidoId) {

        itemPedido.setItem(itemService.findById(itemPedido.getItem().getId()));

        return pedidoService.adicionarItemPedido(itemPedido, pedidoId);

    }
}
