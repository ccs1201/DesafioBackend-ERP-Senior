package com.ccs.erp.api.model.input.IDs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Schema(name = "Item ID Input", description = "Representação do ID de item utilizado em associações como em Item Pedido")
public class ItemIdInput {

    @NotNull
    private UUID id;

}
