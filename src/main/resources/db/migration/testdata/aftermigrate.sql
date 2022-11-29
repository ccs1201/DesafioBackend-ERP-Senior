-- Itens

delete from item;

insert into item (id,ativo, nome, tipo_item, valor)
values
    (gen_random_uuid (),true,'diaria bangalo','SERVICO', 999.90 ),
    (gen_random_uuid (),true,'diaria bangalo especial', 'SERVICO', 1399.90),
    (gen_random_uuid (),true,'diaria bangalo esmeralda', 'SERVICO', 2499.90),
    (gen_random_uuid (),true,'ar condicionado split', 'PRODUTO', 1434.68),
    (gen_random_uuid (),true,'cama box king size', 'PRODUTO', 3478.54),
    (gen_random_uuid (),true,'cama box queen size', 'PRODUTO', 2889.64);