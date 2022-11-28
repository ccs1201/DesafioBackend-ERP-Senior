package com.ccs.erp.domain.repository;

import com.ccs.erp.domain.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    @Query(value = "select p from Pedido p join fetch p.itensPedido ip join fetch ip.item order by p.dataPedido",
            countQuery = "select count (p) from Pedido p")
    Page<Pedido> findAll(Pageable pageable);

    @Override
    @Query(value = "select p from Pedido p left join fetch p.itensPedido ip left join fetch ip.item order by p.dataPedido")
    Optional<Pedido> findById(UUID uuid);
}
