package com.ccs.erp.api.v1.controller;

import com.ccs.erp.api.model.input.PedidoInput;
import com.ccs.erp.api.model.response.PedidoResponse;
import com.ccs.erp.core.utils.mapper.PedidoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/teste")
@RequiredArgsConstructor
@Slf4j
public class TesteController {

    private final PedidoMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    protected PedidoResponse post(@RequestBody @Valid PedidoInput pedidoInput) {
        try {
            var pedido = mapper.toEntity(pedidoInput);
            var response = mapper.toModel(pedido);

            return response;
        } catch (Exception e) {
            log.error("teste controller", e);
        }
        return null;
    }

}
