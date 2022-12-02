create table public.item
(
    id        uuid           not null
        primary key,
    ativo     boolean        not null,
    nome      varchar(150)   not null
        constraint uk_nome_item
            unique,
    tipo_item varchar(255)   not null,
    valor     numeric(19, 2) not null
);

alter table public.item
    owner to postgres;

create table public.pedido
(
    id                   uuid           not null
        primary key,
    data_pedido          timestamp,
    observacao           varchar(150),
    percentual_desconto  integer        not null,
    status_pedido        varchar(255),
    ultima_atualizacao   timestamp,
    valor_total_desconto numeric(19, 2),
    valor_total_itens    numeric(19, 2) not null,
    valor_total_pedido   numeric(19, 2)
);

alter table public.pedido
    owner to postgres;

create table public.item_pedido
(
    id               uuid                        not null
        primary key,
    quantidade       integer                     not null,
    valor_desconto   numeric(19, 2) default 0.00 not null,
    valor_total_item numeric(19, 2)              not null,
    valor_unitario   numeric(19, 2)              not null,
    item_id          uuid                        not null
        constraint fk_item_id
            references public.item,
    pedido_id        uuid
        constraint fk_pedido_id
            references public.pedido ON DELETE CASCADE
);

alter table public.item_pedido
    owner to postgres;

