package com.ccs.erp.domain.service;

import com.ccs.erp.core.exception.RepositoryEntityNotFoundException;
import com.ccs.erp.domain.entity.ItemPedido;
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

    public Page<ItemPedido> findAll(Pageable pageable) {

        var itens = repository.findAll(pageable);
        if (itens.isEmpty()) {
            throw new RepositoryEntityNotFoundException("Nenhum item localizado.");
        }
        return itens;
    }


    public ItemPedido findById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new RepositoryEntityNotFoundException(String
                        .format("Item Pedido com id: %s, não existe.", id)));
    }
}
