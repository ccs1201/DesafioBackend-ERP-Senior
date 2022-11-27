package com.ccs.erp.domain.service;

import com.ccs.erp.domain.entity.Pedido;
import com.ccs.erp.domain.repository.PedidoRepository;
import com.ccs.erp.infrastructure.exception.PedidoNaoEncontradoException;
import com.ccs.erp.infrastructure.exception.RepositoryEntityPersistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository repository;

    @Transactional(readOnly = true)
    public Page<Pedido> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Pedido findById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new PedidoNaoEncontradoException(id));
    }

    @Transactional
    public Pedido save(Pedido pedido) {
        try {
            return repository.save(pedido);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao salvar pedido", e);
            throw new RepositoryEntityPersistException("Erro ao Salvar Pedido", e);
        }
    }

    @Transactional
    public void deleteById(UUID id) {
        var pedido = this.findById(id);
        repository.delete(pedido);
    }

    @Transactional
    public Pedido update(UUID id, Pedido pedido) {
        var pedidoBd = this.findById(id);

        BeanUtils.copyProperties(pedido, pedidoBd, "id");

        return this.save(pedidoBd);
    }

    @Transactional
    public void fecharPedido(UUID id) {
        var pedido = this.findById(id);
        pedido.fechar();
        this.save(pedido);
    }

    @Transactional
    public void abrirPedido(UUID id) {
        var pedido = this.findById(id);
        pedido.abrir();
        this.save(pedido);
    }
}
