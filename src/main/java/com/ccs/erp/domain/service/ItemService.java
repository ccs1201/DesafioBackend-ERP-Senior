package com.ccs.erp.domain.service;

import com.ccs.erp.domain.entity.Item;
import com.ccs.erp.domain.repository.ItemRepository;
import com.ccs.erp.infrastructure.exception.ItemJaCadastradoException;
import com.ccs.erp.infrastructure.exception.ItemNaoEncontradoException;
import com.ccs.erp.infrastructure.exception.ItemUpdateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
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
        }

    }

    @Transactional
    public void deleteById(UUID uuid) {
        var item = this.findById(uuid);

        repository.delete(item);
    }

    @Transactional
    public Item update(Item item, UUID uuid) {

        var itemNoBanco = this.findById(uuid);
        BeanUtils.copyProperties(item, itemNoBanco, "id");

        try {
            return repository.save(itemNoBanco);

        } catch (ConstraintViolationException | DataIntegrityViolationException | IllegalArgumentException e) {
            log.error("Erro ao atualizar Item", e);
            throw new ItemUpdateException(
                    "Erro ao atualizar Item, verifique se já existe um item cadastrado com este nome.", e);
        }


    }
}