package com.ccs.erp.api.v1.controller;

import com.ccs.erp.api.model.input.PedidoInput;
import com.ccs.erp.api.model.response.PedidoResponse;
import com.ccs.erp.api.v1.controller.documentation.PedidoControllerDoc;
import com.ccs.erp.domain.service.PedidoService;
import com.ccs.erp.infrastructure.utils.mapper.PedidoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RestController
@RequestMapping(value = "/api/v1/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Schema(name = "Pedidos", description = "EndPoints de Pedidos")
public class PedidoController implements PedidoControllerDoc {

    private final PedidoService service;
    private final PedidoMapper mapper;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<Page<PedidoResponse>> getAll(@Nullable @PageableDefault(size = 5) Pageable pageable) {

        return supplyAsync(() ->
                service.findAll(pageable), ForkJoinPool.commonPool())
                .thenApply(mapper::toPage);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<PedidoResponse> getById(@PathVariable UUID id) {
        return supplyAsync(() ->
                service.findById(id), ForkJoinPool.commonPool())
                .thenApply(mapper::toModel);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<PedidoResponse> cadastrarNovoPedido(@RequestBody @Valid PedidoInput pedidoInput) {
        return supplyAsync(() ->
                service.CadastrarNovoPedido(mapper.toEntity(pedidoInput)), ForkJoinPool.commonPool())
                .thenApply(mapper::toModel);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        runAsync(() ->
                service.deleteById(id), ForkJoinPool.commonPool()
        );
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<PedidoResponse> update(@RequestBody @Valid PedidoInput pedidoInput, @PathVariable UUID id) {
        return supplyAsync(() ->
                service.update(id, mapper.toEntity(pedidoInput)), ForkJoinPool.commonPool())
                .thenApply(mapper::toModel);
    }

    @Override
    @PatchMapping("/{id}/aberto")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<Void> aberto(@PathVariable UUID id) {

        runAsync(() ->
                service.abrirPedido(id), ForkJoinPool.commonPool()
        );
        return null;
    }

    @Override
    @PatchMapping("/{id}/fechado")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<Void> fechado(@PathVariable UUID id) {
        runAsync(() ->
                service.fecharPedido(id), ForkJoinPool.commonPool()
        );
        return null;
    }
}
