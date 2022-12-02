package com.ccs.erp.api.v1.controller;

import com.ccs.erp.api.model.input.ItemInput;
import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.api.v1.controller.documentation.ItemControllerDoc;
import com.ccs.erp.core.utils.mapper.ItemMapper;
import com.ccs.erp.domain.entity.TipoItem;
import com.ccs.erp.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RestController
@RequestMapping(value = "/api/v1/itens", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ItemController implements ItemControllerDoc {

    private final ItemService service;
    private final ItemMapper mapper;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Page<ItemResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size,
                                                        @RequestParam(defaultValue = "nome") String sort,
                                                        @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        return supplyAsync(() -> service.findAll(PageRequest.of(page, size, direction, sort)),
                ForkJoinPool.commonPool()).thenApply(mapper::toPage);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ItemResponse> getById(@PathVariable UUID id) {

        return supplyAsync((() -> service.findById(id)), ForkJoinPool.commonPool()).thenApply(mapper::toModel);
    }

    @Override
    @PostMapping("/servico")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<ItemResponse> cadastrarServico(@RequestBody @Valid ItemInput itemInput) {

        return supplyAsync(() -> service.cadastrarServico(mapper.toEntity(itemInput)), ForkJoinPool.commonPool()).thenApply(mapper::toModel);
    }

    @PostMapping("/produto")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<ItemResponse> cadastrarProduto(@RequestBody @Valid ItemInput itemInput) {
        return supplyAsync(() ->
                service.cadastrarProduto(mapper.toEntity(itemInput)),
                ForkJoinPool.commonPool()).thenApply(mapper::toModel);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<Void> excluir(@PathVariable UUID id) {

        return runAsync(() ->
                service.deleteById(id), ForkJoinPool.commonPool());
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ItemResponse> atualizar(@RequestBody @Valid ItemInput itemInput, @PathVariable UUID id) {
        return supplyAsync(() ->
                service.update(mapper.toEntity(itemInput), id),
                ForkJoinPool.commonPool()).thenApply(mapper::toModel);
    }

    @Override
    @PatchMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<Void> ativar(@PathVariable UUID id) {
        runAsync(() ->
                service.ativar(id), ForkJoinPool.commonPool());

        return null;
    }

    @Override
    @PatchMapping("/{id}/inativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<Void> inativar(@PathVariable UUID id) {

        runAsync(() ->
                service.inativar(id), ForkJoinPool.commonPool());
        return null;
    }


    @Override
    @GetMapping("/filtro")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Page<ItemResponse>> filtrar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @Nullable String nome,
            @Nullable TipoItem tipo,
            @Nullable Boolean ativo) {
        return supplyAsync(() ->
                service.filtrar(PageRequest.of(page, size, direction, sort), nome, tipo, ativo))
                .thenApply(mapper::toPage);
    }
}
