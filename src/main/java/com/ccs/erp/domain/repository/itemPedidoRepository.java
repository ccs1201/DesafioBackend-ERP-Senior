package com.ccs.erp.domain.repository;

import com.ccs.erp.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface itemPedidoRepository extends JpaRepository<ItemPedido, UUID> {
}
