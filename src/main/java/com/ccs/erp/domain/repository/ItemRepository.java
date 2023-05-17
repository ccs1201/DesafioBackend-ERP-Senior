package com.ccs.erp.domain.repository;

import com.ccs.erp.domain.entity.Item;
import com.ccs.erp.domain.entity.TipoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    String FIND_BY_QUERY = """
            from Item i where (:nome is null or lower(i.nome) like lower(concat('%', :nome, '%')))
            and 
            (:ativo is null or i.ativo= :ativo) 
            and 
            (:tipoItem is null or i.tipoItem= :tipoItem)  order by i.nome
            """;

    @Query(FIND_BY_QUERY)
    Page<Item> findBy(Pageable pageable, String nome, TipoItem tipoItem, Boolean ativo);


}
