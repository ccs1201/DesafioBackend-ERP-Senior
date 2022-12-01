package com.ccs.erp.api.v1.controller.documentation;

import com.ccs.erp.api.model.input.ItemPedidoInput;
import com.ccs.erp.api.model.input.PedidoInput;
import com.ccs.erp.api.model.response.PedidoResponse;
import com.ccs.erp.core.exceptionhandler.model.ApiExceptionResponse;
import com.ccs.erp.core.exceptionhandler.model.ApiValidationErrorResponse;
import com.ccs.erp.domain.entity.StatusPedido;
import com.ccs.erp.domain.entity.TipoItem;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
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
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Se nenhum pedido for encontrado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<Page<PedidoResponse>> getAll(int page, int size, String sort, Sort.Direction direction);

    @Operation(summary = "Filtrar", description = "Filtra os pedidos com base nos parãmetros informados",
            parameters = {

                    @Parameter(name = "page", description = "Numero da pagina a se buscar, começando em ZERO", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de elementos por página", example = "5"),
                    @Parameter(name = "sort", description = "Atributo que será baseada a ordenação ", example = "dataPedido"),
                    @Parameter(name = "direction", description = "Ordenação Ascendente ou Descendente",
                            schema = @Schema(implementation = Sort.Direction.class))
            })
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Se nenhum pedido for encontrado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<Page<PedidoResponse>> filtrar(int page, int size, String sort, Sort.Direction direction,
                                                    String nome, TipoItem tipo, StatusPedido statusPedido);

    @Operation(summary = "Encontrar", description = "Recupera um pedido pelo seu ID")
    @Parameters({
            @Parameter(name = "id", description = "ID do Pedido a ser encontrado",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Quando o pedido não existir",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<PedidoResponse> getById(UUID id);

    @Operation(summary = "Cadastrar", description = "Cadastra um Pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Quando um ou mais atributo inválidos forem informados",
                    content = @Content(schema = @Schema(implementation = ApiValidationErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Quando o formato de dados for inválido",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<PedidoResponse> cadastrarNovoPedido(PedidoInput pedidoInput);

    @Operation(summary = "Remover", description = "Excluí um Pedido")
    @Parameter(name = "id", description = "ID do pedido a ser removido",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Quando o pedido for removido com exito"),
            @ApiResponse(responseCode = "404", description = "Quando o pedido não existir",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    ResponseEntity<Void> excluir(UUID id);

    @Operation(summary = "Atualizar", description = "Atualiza um Pedido.", deprecated = true,
            externalDocs = @ExternalDocumentation(description = "Use Adicionar Item", url = "/swagger-ui/index.html#/Pedidos/adicionarItem"))
    @Parameter(name = "id", description = "ID do pedido a ser atualizado",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    @ApiResponses({
            @ApiResponse(responseCode = "304", description = "Depreciado, use",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<Void> atualizar(PedidoInput pedidoInput,
                                      UUID id);

    @Operation(summary = "Abrir", description = "Abre o Pedido")
    @Parameter(name = "id", description = "ID do Pedido a ser aberto",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Quando o pedido não existir",
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
    @Parameters({
            @Parameter(name = "id", description = "ID do pedido que será aplicado o desconto",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH),
            @Parameter(name = "percentual", description = "Percentual do desconto",
                    example = "15")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se o desconto for aplicado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Se o ID do pedido for inválido",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "422", description = "Se o desconto não puder ser aplicado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<PedidoResponse> aplicarDescontoSomenteProduto(UUID id, Integer percentual);

    @Operation(summary = "Remover Item", description = "Remove um item do pedido")
    @Parameters({
            @Parameter(name = "idPedido", description = "ID do pedido que tera o item removido",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH),
            @Parameter(name = "idItemPedido", description = "ID do ItemPedido que será removido do pedido"
                    , example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se o item for removido com sucesso"),
            @ApiResponse(responseCode = "412", description = "Se o item não pertencer ao pedido",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<PedidoResponse> removerItem(@PathVariable UUID idPedido,
                                                  @PathVariable UUID idItemPedido);

    @Operation(summary = "Adicionar Item", description = "Adiciona um item a um pedido existente")
    @Parameter(name = "id", description = "ID do pedido que terá o item adicionado",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", in = ParameterIn.PATH)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quando o item for adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Quando um ou mais atributo inválidos forem informados",
                    content = @Content(schema = @Schema(implementation = ApiValidationErrorResponse.class))),
            @ApiResponse(responseCode = "412", description = "Quando o item não puder ser adicionado ao pedido",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<PedidoResponse> adicionarItem(@PathVariable UUID id,
                                                    @RequestBody @Valid ItemPedidoInput itemPedidoInput);
}
