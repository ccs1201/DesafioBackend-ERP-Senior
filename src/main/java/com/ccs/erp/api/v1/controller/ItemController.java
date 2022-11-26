package com.ccs.erp.api.v1.controller;

import com.ccs.erp.api.model.input.ItemInput;
import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.api.v1.controller.documentation.ItemControllerDoc;
import com.ccs.erp.domain.service.ItemService;
import com.ccs.erp.infrastructure.utils.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController implements ItemControllerDoc {

    private final ItemService service;
    private final ItemMapper mapper;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Page<ItemResponse>> getAll(@Nullable @PageableDefault Pageable pageable) {

        return supplyAsync(() ->
                mapper.toPage(service.findAll(pageable))
        );
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ItemResponse> getById(@PathVariable UUID id) {

        return supplyAsync((() ->
                mapper.toModel(service.findById(id))
        ));
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ItemResponse> save(@RequestBody @Valid ItemInput itemInput) {

        return supplyAsync(() ->
                mapper.toModel(
                        service.save(mapper.toEntity(itemInput))
                )
        );
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        runAsync(() ->
                service.deleteById(id)
        );
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ItemResponse> update(@RequestBody @Valid ItemInput itemInput,
                                                  @PathVariable UUID id) {
        return supplyAsync(() ->
                mapper.toModel(
                        service.update(
                                mapper.toEntity(itemInput), id)
                )

        );
    }

    @Override
    @PatchMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> ativar(@PathVariable UUID id) {
        runAsync(() ->
                service.ativar(id)
        );
        return null;
    }

    @Override
    @PatchMapping("/{id}/inativo")
    public ResponseEntity<Void> inativar(@PathVariable UUID id) {

        runAsync(() ->
                service.inativar(id)
        );
        return null;
    }

}
