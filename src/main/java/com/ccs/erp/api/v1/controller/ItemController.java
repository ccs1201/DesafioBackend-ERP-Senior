package com.ccs.erp.api.v1.controller;

import com.ccs.erp.api.model.input.ItemInput;
import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.domain.entity.Item;
import com.ccs.erp.domain.service.ItemService;
import com.ccs.erp.infrastructure.utils.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.*;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;
    private final ItemMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    protected CompletableFuture<Page<ItemResponse>> getAll(@PageableDefault(size = 5) Pageable pageable) {

        return supplyAsync(() ->
                mapper.toPage(service.findAll(pageable))
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    protected CompletableFuture<ItemResponse> save(@RequestBody @Valid ItemInput itemInput) {

        return supplyAsync(() ->
                mapper.toResponseModel(
                        service.save(mapper.toEntity(itemInput))
                )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected void delete(@PathVariable UUID id) {

        runAsync(() ->
                service.deleteById(id)
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    protected CompletableFuture<ItemResponse> update(@RequestBody @Valid ItemInput itemInput,
                                                     @PathVariable UUID id) {
        return supplyAsync(() ->
                mapper.toResponseModel(
                        service.update(
                                mapper.toEntity(itemInput), id)
                )

        );
    }


}
