package com.ccs.erp.api.v1.controller;

import com.ccs.erp.api.model.response.ItemPedidoResponse;
import com.ccs.erp.core.utils.mapper.ItemPedidoMapper;
import com.ccs.erp.domain.service.ItemPedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@RestController
@RequestMapping("/api/v1/itensPedidos")
@RequiredArgsConstructor
public class ItemPedidoController {
    private final ItemPedidoMapper mapper;
    private final ItemPedidoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Page<ItemPedidoResponse>> getAll(@Nullable @RequestParam(defaultValue = "0") int page,
                                                              @Nullable @RequestParam(defaultValue = "5") int size,
                                                              @Nullable @RequestParam(defaultValue = "nome") String sort,
                                                              @Nullable @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        return supplyAsync(() -> service.findAll(
                PageRequest.of(page, size, direction, sort)
        ), ForkJoinPool.commonPool())
                .thenApply(mapper::toPage);
    }

    @GetMapping("/{id}")
    @ResponseStatus
    public CompletableFuture<ItemPedidoResponse> findById(@PathVariable UUID id){
        return supplyAsync(()->
                service.findById(id), ForkJoinPool.commonPool()
        ).thenApply(mapper::toModel);
    }

}
