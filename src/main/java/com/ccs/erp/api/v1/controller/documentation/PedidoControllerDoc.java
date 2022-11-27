package com.ccs.erp.api.v1.controller.documentation;

import com.ccs.erp.api.model.input.PedidoInput;
import com.ccs.erp.api.model.response.PedidoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Pedidos")
public interface PedidoControllerDoc {

    @Operation(summary = "Listar", description = "Lista os Pedidos cadastrados com paginação, se nenhum parâmetro" +
            "for passado para paginação, serão aplicados os valores padrão")
    @Parameters({
            @Parameter(name = "page", description = "Numero da pagina a se buscar, começando em ZERO, default 0", example = "0"),
            @Parameter(name = "size", description = "Quantidade de elementos por página default 10", example = "5"),
            @Parameter(name = "sort", description = "Atributo que será baseada a ordenação ", example = "item.nome")
    })
    CompletableFuture<Page<PedidoResponse>> getAll(Pageable pageable);

    @Operation(summary = "Encontrar", description = "Recupera um pedido pelo seu ID")
    @Parameters({
            @Parameter(name = "id", description = "ID do Pedido a ser encontrado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    })
    CompletableFuture<PedidoResponse> getById(UUID id);

    @Operation(summary = "Cadastrar", description = "Cadastra um Pedido")
    CompletableFuture<PedidoResponse> save(PedidoInput pedidoInput);

    @Operation(summary = "Excluir", description = "Excluí um Pedido somente")
    @Parameter(name = "id", description = "ID do pedido a ser removido", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    ResponseEntity<Void> delete(UUID id);

    @Operation(summary = "Atualizar", description = "Atualiza os atributos de um Pedido.")
    @Parameter(name = "id", description = "ID do pedido a ser atualizado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    CompletableFuture<PedidoResponse> update(PedidoInput pedidoInput,
                                           UUID id);

    @Operation(summary = "Abrir", description = "Abre o Pedido")
    @Parameter(name = "id", description = "ID do Pedido a ser aberto")
    CompletableFuture<Void> aberto(UUID id);

    @Operation(summary = "Fechar", description = "Fecha um Pedido")
    @Parameter(name = "id", description = "ID do Pedido a ser fechado")
    CompletableFuture<Void> fechado(UUID id);
}
