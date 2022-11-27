package com.ccs.erp.api.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Root Entry Point")
public class RootEntryPointControllerV1 {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Root Entrypoint", description = "Fornece o link de todos os recursos da API (Implementação de HyperMedia/HATEAOS)")
    protected CompletableFuture<RootEntryPointModelV1> getRootEntrypoint() {

        return supplyAsync(() -> {
                    var root = new RootEntryPointModelV1();

                    root.add(linkTo(ItemController.class).withRel("itens"))
                            .add(linkTo(PedidoController.class).withRel("pedidos"));

                    return root;
                }, ForkJoinPool.commonPool()
        );

    }

    private static final class RootEntryPointModelV1 extends RepresentationModel<RootEntryPointModelV1> {
    }

}
