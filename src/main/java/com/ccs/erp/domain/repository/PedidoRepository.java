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

    final String FIND_ALL_QUERY = """
            select p from Pedido p 
            join fetch p.itensPedido ip 
            join fetch ip.item  
            order by p.dataPedido
            """;

    final String COUNT_QUERY = """
            select count (p) from Pedido p
            """;

    final String FIND_BY_ID_QUERY = """
            select p from Pedido p 
            left join fetch p.itensPedido ip 
            left join fetch ip.item 
            where p.id= :uuid 
            order by p.dataPedido
            """;

    @Query(value = FIND_ALL_QUERY,
            countQuery= COUNT_QUERY)
    Page<Pedido> findAll(Pageable pageable);

    @Override
    @Query(value = FIND_BY_ID_QUERY)
    Optional<Pedido> findById(UUID uuid);
}
