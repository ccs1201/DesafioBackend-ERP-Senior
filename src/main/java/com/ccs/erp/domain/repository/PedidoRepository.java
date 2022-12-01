package com.ccs.erp.domain.repository;

import com.ccs.erp.domain.entity.Pedido;
import com.ccs.erp.domain.entity.StatusPedido;
import com.ccs.erp.domain.entity.TipoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    String FIND_ALL_QUERY = """
            select p from Pedido p 
            join fetch p.itensPedido itensPedido 
            join fetch itensPedido.item item
            """;
    String FILTER_QUERY = """
            select p from Pedido p 
            join fetch p.itensPedido ip 
            join fetch ip.item i
            where (:nome is null or i.nome like lower(concat('%', :nome, '%')))
            and 
            (:tipoItem is null or i.tipoItem= :tipoItem) 
            and
            (:statusPedido is null or p.statusPedido= :statusPedido)
            order by p.dataPedido
            """;

    String COUNT_QUERY = """
            select count (p) from Pedido p
            """;

    String FIND_BY_ID_QUERY = """
            select p from Pedido p 
            left join fetch p.itensPedido itensPedido 
            left join fetch itensPedido.item item
            where p.id= :uuid 
            """;

    @Query(value = FIND_ALL_QUERY,
            countQuery = COUNT_QUERY)
    Page<Pedido> findAll(Pageable pageable);

    @Override
    @Query(value = FIND_BY_ID_QUERY)
    Optional<Pedido> findById(UUID uuid);

    @Query(value = FILTER_QUERY, countQuery = COUNT_QUERY)
    Page<Pedido> filtrar(Pageable pageable, String nome, TipoItem tipoItem, StatusPedido statusPedido);
}
