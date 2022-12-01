package com.ccs.erp.api.v1.controller.documentation;

import com.ccs.erp.api.model.response.ItemPedidoResponse;
import com.ccs.erp.core.exceptionhandler.model.ApiExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Itens Pedidos")
public interface ItemPedidoControllerDoc {

    @Operation(summary = "Listar", description = "lista todos os Itens Pedido",
            parameters = {
                    @Parameter(name = "page", description = "Numero da pagina", allowEmptyValue = true),
                    @Parameter(name = "size", description = "Quantidade de Itens por página", allowEmptyValue = true),
                    @Parameter(name = "sort", description = "Atributo para ordenação do resultado", example = "nome", allowEmptyValue = true),
                    @Parameter(name = "direction", description = "Sentido de ordenação")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Se nenhum item for encontrado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class)))
    })
    CompletableFuture<Page<ItemPedidoResponse>> getAll(@Nullable @RequestParam(defaultValue = "0") int page,
                                                       @Nullable @RequestParam(defaultValue = "5") int size,
                                                       @Nullable @RequestParam(defaultValue = "nome") String sort,
                                                       @Nullable @RequestParam(defaultValue = "ASC") Sort.Direction direction);

    @GetMapping("/{id}")
    @ResponseStatus
    CompletableFuture<ItemPedidoResponse> findById(@PathVariable UUID id);
}
