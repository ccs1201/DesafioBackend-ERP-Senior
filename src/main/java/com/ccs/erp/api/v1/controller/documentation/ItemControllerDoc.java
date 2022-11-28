package com.ccs.erp.api.v1.controller.documentation;

import com.ccs.erp.api.model.input.ItemInput;
import com.ccs.erp.api.model.response.ItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Itens")
public interface ItemControllerDoc {

    @Operation(summary = "Listar", description = "Lista os Items cadastrados com paginação, se nenhum parâmetro" +
            "for passado para paginação, serão aplicados os valores padrão")
    @Parameters({
            @Parameter(name = "page", description = "Numero da pagina a se buscar, começando em ZERO, default 0", example = "0"),
            @Parameter(name = "size", description = "Quantidade de elementos por página default 10", example = "5"),
            @Parameter(name = "sort", description = "Atributo que será baseada a ordenação ", example = "nome")
    })
    CompletableFuture<Page<ItemResponse>> getAll(Pageable pageable);


    @Operation(summary = "Encontrar", description = "Busca um Item pelo seu ID")
    @Parameters({
            @Parameter(name = "id", description = "ID do item a ser encontrado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    })
    CompletableFuture<ItemResponse> getById(UUID id);

    @Operation(summary = "Cadastrar Serviço", description = "Cadastra um Item como Serviço")
    CompletableFuture<ItemResponse> cadastrarServico(ItemInput itemInput);

    @Operation(summary = "Cadastrar Produto", description = "Cadastra um Item como Produto")
    CompletableFuture<ItemResponse> cadastrarProduto(ItemInput itemInput);

    @Operation(summary = "Excluir", description = "Excluí um item somente se ele não estiver a associado a um pedido")
    @Parameter(name = "id", description = "ID do item a ser removido", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    CompletableFuture<Void> delete(UUID id);

    @Operation(summary = "Atualizar", description = "Atualiza os atributos de um Item.")
    @Parameter(name = "id", description = "ID do item a ser atualizado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    CompletableFuture<ItemResponse> update(ItemInput itemInput,
                                           UUID id);

    @Operation(summary = "Ativa", description = "Ativa um item")
    @Parameter(name = "id", description = "ID do item a ser ativado")
    CompletableFuture<Void> ativar(UUID id);

    @Operation(summary = "Inativa", description = "Inativa um item")
    @Parameter(name = "id", description = "ID do item a ser inativado")
    CompletableFuture<Void> inativar(UUID id);
}
