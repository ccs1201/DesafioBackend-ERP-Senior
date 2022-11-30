package com.ccs.erp.domain.service;

import com.ccs.erp.core.exception.*;
import com.ccs.erp.domain.entity.Item;
import com.ccs.erp.domain.entity.ItemPedido;
import com.ccs.erp.domain.entity.Pedido;
import com.ccs.erp.domain.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemService itemService;


    public Page<Pedido> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }


    public Pedido findById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new PedidoNaoEncontradoException(id));
    }

    /**
     * <p>Método privado para salvar um {@link Pedido}
     * deve ser chamado por métodos auxiliares que garantam
     * a consistência dos dados do pedido antes de gravar no banco.</p>
     *
     * @param pedido O Pedido a ser salvo no banco.
     * @return {@link Pedido}
     */

    private Pedido save(Pedido pedido) {
        try {
            return repository.saveAndFlush(pedido);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao salvar pedido", e);
            throw new RepositoryEntityPersistException("Erro ao Salvar Pedido", e);
        }
    }

    @Transactional
    public void deleteById(UUID id) {
        var pedido = repository.getReferenceById(id);
        repository.delete(pedido);
    }

    @Transactional
    public Pedido update(UUID id, Pedido pedido) {
        var pedidoBd = this.findById(id);

        BeanUtils.copyProperties(pedido, pedidoBd, "id");

        return this.save(pedidoBd);
    }

    @Transactional
    public void fecharPedido(UUID id) {
        var pedido = this.findById(id);
        pedido.fechar();
        repository.saveAndFlush(pedido);
    }

    @Transactional
    public void abrirPedido(UUID id) {
        var pedido = this.findById(id);
        pedido.abrir();
        repository.saveAndFlush(pedido);
    }

    /**
     * <p>Cadastra um novo pedido</p>
     * <p><b>Não deve ser usado para atualizar um pedido</b></p>
     *
     * @param pedido
     * @return
     */
    @Transactional
    public Pedido CadastrarNovoPedido(Pedido pedido) {

        //seta o status do pedido como ABERTO
        pedido.abrir();

        //Busca os Itens que compõem o pedido
        getItens(pedido);

        pedido.calcularTotais();

        //Grava no Banco e retorna
        return this.save(pedido);

    }

    /**
     * <p>Busca no banco os Itens do pedido e seta
     * no {@link Item} do <br>
     * {@link  ItemPedido}</p>
     *
     * @param pedido O pedido que precisa dos itens populados
     */
    private void getItens(Pedido pedido) {

        Set<Item> itensInvalidos = new HashSet<>();

        try {
            pedido.getItensPedido().forEach((itemPedido -> {

                var item = itemService.findById(itemPedido.getItem().getId());

                //O item só pode ser associado a um pedido se estiver ativo
                if (item.getAtivo()) {
                    itemPedido.setItem(item);
                    //Se for invalido adiciona no set
                } else {
                    itensInvalidos.add(item);
                }
            }
            ));

            //Agora verificamos se o set contém itens
            //e lancaçamos uma exception informando os itens inválidos
            verificaItensInvalido(itensInvalidos);

        } catch (ItemNaoEncontradoException e) {
            log.error("Erro no método getItens em PedidoService", e);
            throw new PedidoException(e.getMessage());
        }
    }

    /**
     * <p>Verifica se há itens inativos no pedido.</p>
     *
     * @param itensInvalidos Set contente os itens inválidos
     * @throws PedidoException caso haja algum item inativo
     */
    private static void verificaItensInvalido(Set<Item> itensInvalidos) {

        if (!itensInvalidos.isEmpty()) {
            StringBuilder message = new StringBuilder();

            itensInvalidos.forEach(item ->
                    message.append(String
                            .format("O Item %s, está invativo e não pôde ser adicionado ao pedido.", item.getNome())
                    )
            );
            throw new PedidoException(message.toString());
        }
    }

    @Transactional
    public Pedido aplicarDescontoSomenteProduto(UUID id, Integer percentual) {

        var pedido = this.findById(id);

        pedido.aplicarDesconto(percentual);

        return save(pedido);
    }
}
