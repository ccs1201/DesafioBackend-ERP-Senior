package com.ccs.erp.domain.repository;

import com.ccs.erp.domain.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    @Query(value = "select p from Pedido p  left join fetch p.itensPedido ip left join fetch ip.item i order by p.dataPedido, i.nome",
            countQuery = "select p from Pedido p")
    Page<Pedido> findAll(Pageable pageable);
}
