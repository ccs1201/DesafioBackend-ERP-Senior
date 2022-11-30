package com.ccs.erp.api.v1.controller.documentation;

import com.ccs.erp.api.model.input.ItemInput;
import com.ccs.erp.api.model.response.ItemResponse;
import com.ccs.erp.core.exceptionhandler.model.ApiExceptionResponse;
import com.ccs.erp.domain.entity.TipoItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Itens")
public interface ItemControllerDoc {

    @Operation(summary = "Listar", description = "Lista todos os Items cadastrados com paginação, se nenhum parâmetro" +
            "for passado para paginação, serão aplicados os valores padrão")
    @Parameters({
            @Parameter(name = "page", description = "Numero da pagina a se buscar, começando em ZERO", example = "0"),
            @Parameter(name = "size", description = "Quantidade de elementos por página", example = "5"),
            @Parameter(name = "sort", description = "Atributo que será baseada a ordenação", example = "nome"),
            @Parameter(name = "direction", description = "Ordenação Ascendente ou Descendente")
    })
    CompletableFuture<Page<ItemResponse>> getAll(int page, int size, String sort, Sort.Direction direction);


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
    @ApiResponse(responseCode = "409", description = "Se o item não puder ser excluido",
            content = @Content(
                    schema = @Schema(implementation = ApiExceptionResponse.class)))
    CompletableFuture<Void> excluir(UUID id);

    @Operation(summary = "Atualizar", description = "Atualiza um Item.")
    @Parameter(name = "id", description = "ID do item a ser atualizado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    CompletableFuture<ItemResponse> atualizar(ItemInput itemInput,
                                              UUID id);

    @Operation(summary = "Ativo", description = "Ativa um item")
    @Parameter(name = "id", description = "ID do item a ser ativado")
    CompletableFuture<Void> ativar(UUID id);

    @Operation(summary = "Inativo", description = "Inativa um item")
    @Parameter(name = "id", description = "ID do item a ser inativado")
    CompletableFuture<Void> inativar(UUID id);

    @Operation(summary = "Busca itens com por critérios dinâmicos",
            description = "Efetua consulta de Itens por nome, tipoItem e ativo",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "nome", description = "Nome do item", example = "xbox one s", allowEmptyValue = true),
                    @Parameter(in = ParameterIn.QUERY, name = "tipo", description = "O tipo de item", allowEmptyValue = true),
                    @Parameter(in = ParameterIn.QUERY, name = "ativo", description = "Buscar por ativo = true, inativo = false ou todos = null", allowEmptyValue = true),
                    @Parameter(name = "page", description = "Numero da pagina", allowEmptyValue = true),
                    @Parameter(name = "size", description = "Quantidade de Itens por página", allowEmptyValue = true),
                    @Parameter(name = "sort", description = "Atributo para ordenação do resultado", example = "nome", allowEmptyValue = true),
            })
    CompletableFuture<Page<ItemResponse>> filtrar(int page, int size, String sort, Sort.Direction direction, String nome, TipoItem tipo, Boolean ativo);
}
