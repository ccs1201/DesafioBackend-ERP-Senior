package com.ccs.erp.api.v1.controller;

import com.ccs.erp.domain.entity.Pedido;
import com.ccs.erp.domain.repository.PedidoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PedidoControllerTest {

    private final String BASE_URI = "/api/vi/pedidos";
    private Pedido pedido;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PedidoRepository pedidoRepository;

    @BeforeAll
    void setup() {

        var pageable = PageRequest.of(1, 1);

        pedido = pedidoRepository.findAll(pageable).toList().get(0);
    }

    @Test
    @DisplayName("Testa cadastrar novo Pedido, deve retornar 201 CREATED")
    void cadastrarNovoPedido() throws Exception {


    }

    @Test
    void aplicarDescontoSomenteProduto() {
    }
}