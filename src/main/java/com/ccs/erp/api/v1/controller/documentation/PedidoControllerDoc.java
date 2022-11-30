package com.ccs.erp.api.v1.controller.documentation;

import com.ccs.erp.api.model.input.PedidoInput;
import com.ccs.erp.api.model.response.PedidoResponse;
import com.ccs.erp.core.exceptionhandler.model.ApiExceptionResponse;
import com.ccs.erp.core.exceptionhandler.model.ApiValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Pedidos")
public interface PedidoControllerDoc {

    @Operation(summary = "Listar", description = "Lista todos os Pedidos cadastrados com paginação, se nenhum parâmetro" +
            "for passado para paginação, serão aplicados os valores padrão")
    @Parameters({
            @Parameter(name = "page", description = "Numero da pagina a se buscar, começando em ZERO", example = "0"),
            @Parameter(name = "size", description = "Quantidade de elementos por página", example = "5"),
            @Parameter(name = "sort", description = "Atributo que será baseada a ordenação ", example = "dataPedido"),
            @Parameter(name = "direction", description = "Ordenação Ascendente ou Descendente",
                    schema = @Schema(implementation = Sort.Direction.class))
    })
    CompletableFuture<Page<PedidoResponse>> getAll(int page, int size, String sort, Sort.Direction direction);

    @Operation(summary = "Encontrar", description = "Recupera um pedido pelo seu ID")
    @Parameters({
            @Parameter(name = "id", description = "ID do Pedido a ser encontrado",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    })
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "404", description = "Quando o pedido não existir",
                            content = @Content(
                                    schema = @Schema(implementation = ApiExceptionResponse.class)))
            })
    CompletableFuture<PedidoResponse> getById(UUID id);

    @Operation(summary = "Cadastrar", description = "Cadastra um Pedido")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "400", description = "Quando um atributo inválido for informado",
                            content = @Content(
                                    schema = @Schema(implementation = ApiValidationErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Quando o formato de dados for inválido",
                            content = @Content(
                                    schema = @Schema(implementation = ApiExceptionResponse.class)
                            ))
            })
    CompletableFuture<PedidoResponse> cadastrarNovoPedido(PedidoInput pedidoInput);

    @Operation(summary = "Excluir", description = "Excluí um Pedido")
    @Parameter(name = "id", description = "ID do pedido a ser removido",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "404", description = "Quando o pedido não existir",
                            content = @Content(
                                    schema = @Schema(implementation = ApiExceptionResponse.class))
                    )
            })
    ResponseEntity<Void> excluir(UUID id);

    @Operation(summary = "Atualizar", description = "Atualiza um Pedido.")
    @Parameter(name = "id", description = "ID do pedido a ser atualizado",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "404", description = "Quandoroduto o pedido não existir",
                            content = @Content(
                                    schema = @Schema(implementation = ApiExceptionResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Quando um atributo ou formato inválido for informado",
                            content = @Content(
                                    schema = @Schema(implementation = ApiValidationErrorResponse.class)
                            )
                    )
            })
    CompletableFuture<PedidoResponse> atualizar(PedidoInput pedidoInput,
                                                UUID id);

    @Operation(summary = "Abrir", description = "Abre o Pedido")
    @Parameter(name = "id", description = "ID do Pedido a ser aberto",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Quando o pedidoroduto não existir",
                    content = @Content(
                            schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "204", description = "Quando o pedido for aberto com sucesso")
    })
    CompletableFuture<Void> abrir(UUID id);

    @Operation(summary = "Fechar", description = "Fecha um Pedido")
    @Parameter(name = "id", description = "ID do Pedido a ser fechado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Quando o pedido não existir",
                    content = @Content(
                            schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "204", description = "Quando o pedido for fechado com sucesso")
    })
    CompletableFuture<Void> fechar(UUID id);

    @Operation(summary = "Desconto Produtos", description = "Aplica o desconto somente nos produtos")
    @Parameters(
            {
                    @Parameter(name = "id", description = "ID do pedido que sera aplicado o desconto",
                            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH),
                    @Parameter(name = "percentual", description = "Percentual do desconto",
                            example = "15")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se o desconto for aplicado com exito"),
            @ApiResponse(responseCode = "404", description = "Se o ID do pedido for inválido",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "422", description = "Se o desconto não puder ser aplicado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<PedidoResponse> aplicarDescontoSomenteProduto(UUID id, Integer percentual);

}
