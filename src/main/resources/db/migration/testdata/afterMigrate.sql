delete from pedido;
delete from item_pedido;
delete from item;

insert into item (id, ativo, nome, tipo_item, valor)
values ('eb397ae2-0974-4c8a-b04d-dd9b63b41f73', true, 'diaria bangalo', 'SERVICO', 999.90),
       ('42332260-a6e3-4d10-b001-21ad333d7148', true, 'diaria bangalo especial', 'SERVICO', 1399.90),
       ('5eee1208-4444-4f5c-9dd8-91e1c45de18b', true, 'diaria bangalo esmeralda', 'SERVICO', 2499.90),
       ('7d619ce6-c553-45e2-a4e1-62bf99844e9c', true, 'ar condicionado split', 'PRODUTO', 1434.68),
       ('7f36afa2-a855-497c-8ed7-0d3c45e54f7b', true, 'XBOX Series X', 'PRODUTO', 3899.99),
       ('791a7f11-d0a2-43a6-86df-ff8db45ca907', true, 'XBOX Series S', 'PRODUTO', 2049.99),
       ('1a85b8e3-1aae-439c-987d-eb82eb96b6f1', true, 'PlayStation 5', 'PRODUTO', 4499.90),
       ('4c59be31-5a1f-42a9-90f4-f7700504b237', true, 'cama box king size', 'PRODUTO', 3478.54),
       ('d581b0c9-fe3a-4c0a-8c3f-d9643361a2c7', true, 'cama box queen size', 'PRODUTO', 2889.64);


insert into pedido (id, data_pedido, observacao, percentual_desconto, status_pedido, ultima_atualizacao,
                    valor_total_desconto, valor_total_itens, valor_total_pedido)
values ('8f3fb247-c68a-487e-b25e-8295a8d125ce', '2022-12-01 21:25:39.092724', 'teste 1', 0, 'ABERTO',
        '2022-12-01 21:25:39.092792', 0.00, 14346.80, 14346.80),
       ('9873f6e7-820c-4a8b-ad92-f557bcccd96e', '2022-12-01 21:26:03.398100', 'teste 2', 0, 'ABERTO',
        '2022-12-01 21:26:03.398133', 0.00, 999.90, 999.90),
       ('713c379d-ba24-49b0-9995-1da34208a92e', '2022-12-01 21:26:15.577676', 'teste 3', 0, 'ABERTO',
        '2022-12-01 21:26:15.577738', 0.00, 1399.90, 1399.90),
       ('0d10547b-4936-49f9-b3d0-3a76b6fd2ec2', '2022-12-01 21:26:29.687678', 'teste 4', 0, 'ABERTO',
        '2022-12-01 21:26:29.687723', 0.00, 2499.90, 2499.90),
       ('24b46676-09b8-4eb7-96cc-012dec1e2398', '2022-12-01 21:26:45.142618', 'teste 5', 0, 'ABERTO',
        '2022-12-01 21:26:45.142664', 0.00, 3899.99, 3899.99),
       ('e5934790-35a4-4729-90e1-63426ae602a6', '2022-12-01 21:26:59.156075', 'teste 6', 0, 'ABERTO',
        '2022-12-01 21:26:59.156119', 0.00, 2049.99, 2049.99),
       ('bccae656-9ee9-41c2-b411-c1896ba571f7', '2022-12-01 21:27:14.990673', 'teste 7', 0, 'ABERTO',
        '2022-12-01 21:27:14.990719', 0.00, 44999.00, 44999.00),
       ('80965a78-74ff-48e2-8a30-2143dd7af64e', '2022-12-01 21:29:41.232620', 'teste 10', 0, 'ABERTO',
        '2022-12-01 21:29:41.232663', 0.00, 61280.06, 61280.06),
       ('8575724b-2c69-4dad-af40-c7467d5adc63', '2022-12-01 21:27:25.782345', 'teste 8', 0, 'ABERTO',
        '2022-12-01 21:27:25.782386', 0.00, 34785.40, 34785.40),
       ('f94f77f2-ec58-48ff-a32c-77adf5ade0f9', '2022-12-01 21:27:47.603428', 'teste 9', 0, 'ABERTO',
        '2022-12-01 21:27:47.603477', 0.00, 28896.40, 28896.40);



insert into item_pedido (id, quantidade, valor_desconto, valor_total_item, valor_unitario, item_id, pedido_id)
values ('492ca707-442f-4631-b607-f107fa7226dc', 10, 0.00, 14346.80, 1434.68, '7d619ce6-c553-45e2-a4e1-62bf99844e9c',
        '8f3fb247-c68a-487e-b25e-8295a8d125ce'),
       ('6d7eb710-5662-4206-80b8-c8709df3219c', 1, 0.00, 999.90, 999.90, 'eb397ae2-0974-4c8a-b04d-dd9b63b41f73',
        '9873f6e7-820c-4a8b-ad92-f557bcccd96e'),
       ('5f0d207f-8fa0-4c41-85b9-9c5c235b5497', 1, 0.00, 1399.90, 1399.90, '42332260-a6e3-4d10-b001-21ad333d7148',
        '713c379d-ba24-49b0-9995-1da34208a92e'),
       ('66bae5ce-603b-469d-af9e-3917caf72a00', 1, 0.00, 2499.90, 2499.90, '5eee1208-4444-4f5c-9dd8-91e1c45de18b',
        '0d10547b-4936-49f9-b3d0-3a76b6fd2ec2'),
       ('064cf3d8-2289-4cab-8698-267c4f01faaf', 1, 0.00, 3899.99, 3899.99, '7f36afa2-a855-497c-8ed7-0d3c45e54f7b',
        '24b46676-09b8-4eb7-96cc-012dec1e2398'),
       ('07c1b7c6-af95-422a-b1c9-551ee0a16c1a', 1, 0.00, 2049.99, 2049.99, '791a7f11-d0a2-43a6-86df-ff8db45ca907',
        'e5934790-35a4-4729-90e1-63426ae602a6'),
       ('eb20c6b4-ff67-455c-9582-a9726fe297f6', 10, 0.00, 44999.00, 4499.90, '1a85b8e3-1aae-439c-987d-eb82eb96b6f1',
        'bccae656-9ee9-41c2-b411-c1896ba571f7'),
       ('b59d1f1b-ec7c-4c10-835a-878bbd8465eb', 10, 0.00, 34785.40, 3478.54, '4c59be31-5a1f-42a9-90f4-f7700504b237',
        '8575724b-2c69-4dad-af40-c7467d5adc63'),
       ('3d36362d-e855-4f87-8d3f-c9c8d9daaac2', 10, 0.00, 28896.40, 2889.64, 'd581b0c9-fe3a-4c0a-8c3f-d9643361a2c7',
        'f94f77f2-ec58-48ff-a32c-77adf5ade0f9'),
       ('25867826-2a56-4296-b0ef-bf87b2feef9e', 10, 0.00, 28896.40, 2889.64, 'd581b0c9-fe3a-4c0a-8c3f-d9643361a2c7',
        '80965a78-74ff-48e2-8a30-2143dd7af64e'),
       ('b3382f8c-52c9-4559-acdb-9010adb26f26', 10, 0.00, 24999.00, 2499.90, '5eee1208-4444-4f5c-9dd8-91e1c45de18b',
        '80965a78-74ff-48e2-8a30-2143dd7af64e'),
       ('d78251c4-0570-427e-836c-9bcb76951ff9', 1, 0.00, 3899.99, 3899.99, '7f36afa2-a855-497c-8ed7-0d3c45e54f7b',
        '80965a78-74ff-48e2-8a30-2143dd7af64e'),
       ('8bf0296a-09a6-4cb5-804b-dc5c0dc0ab7e', 1, 0.00, 1434.68, 1434.68, '7d619ce6-c553-45e2-a4e1-62bf99844e9c',
        '80965a78-74ff-48e2-8a30-2143dd7af64e'),
       ('21b2a4d5-cf47-4f1a-90e7-a94b6c1763db', 1, 0.00, 2049.99, 2049.99, '791a7f11-d0a2-43a6-86df-ff8db45ca907',
        '80965a78-74ff-48e2-8a30-2143dd7af64e');


