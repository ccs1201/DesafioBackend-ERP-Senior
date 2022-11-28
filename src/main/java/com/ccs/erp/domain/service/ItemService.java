package com.ccs.erp.domain.service;

import com.ccs.erp.domain.entity.Item;
import com.ccs.erp.domain.entity.TipoItem;
import com.ccs.erp.domain.repository.ItemRepository;
import com.ccs.erp.infrastructure.exception.ItemJaCadastradoException;
import com.ccs.erp.infrastructure.exception.ItemNaoEncontradoException;
import com.ccs.erp.infrastructure.exception.RepositoryEntityInUseException;
import com.ccs.erp.infrastructure.exception.RepositoryEntityPersistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository repository;

    public Page<Item> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Item findById(UUID uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new ItemNaoEncontradoException(uuid));
    }

    @Transactional
    public Item save(Item item) {
        try {
            return repository.save(item);
        } catch (DataIntegrityViolationException e) {
            log.error("Erro ao salvar Item", e);
            throw new ItemJaCadastradoException(String
                    .format("Item %s, já está cadastrado, por favor verifique.", item.getNome()), e);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao Salvar Item", e);
            throw new RepositoryEntityPersistException("Ocorreu um erro inesperado ao salvar o item", e);
        }
    }

    @Transactional
    public void deleteById(UUID uuid) {
        var item = this.findById(uuid);
    try {
        repository.delete(item);
    } catch (DataIntegrityViolationException e){
        throw new RepositoryEntityInUseException("Não é possível remover o item código: "+ uuid, e);

    }

    }

    @Transactional
    public Item update(Item item, UUID uuid) {
        var itemNoBanco = this.findById(uuid);

        BeanUtils.copyProperties(item, itemNoBanco, "id", "tipoItem");

        return this.save(itemNoBanco);
    }

    @Transactional
    public void ativar(UUID id) {
        var item = this.findById(id);
        item.ativa();
        this.save(item);
    }

    public void inativar(UUID id) {
        var item = this.findById(id);
        item.inativa();
        this.save(item);
    }

    public Item cadastrarProduto(Item item) {
        item.setTipoItem(TipoItem.PRODUTO);
        return this.save(item);
    }

    public Item cadastrarServico(Item item) {
        item.setTipoItem(TipoItem.SERVICO);
        return this.save(item);
    }
}
