package com.ccs.erp.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamicUpdate
public class Item {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(length = 150, unique = true, nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoItem tipoItem;

    @Positive
    @Column(nullable = false)
    private BigDecimal valor;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo;
}
