package com.ccs.erp.domain.service;

import com.ccs.erp.core.exception.ItemNaoEncontradoException;
import com.ccs.erp.core.exception.PedidoException;
import com.ccs.erp.core.exception.PedidoNaoEncontradoException;
import com.ccs.erp.core.exception.RepositoryEntityPersistException;
import com.ccs.erp.domain.desconto.DescontoItem;
import com.ccs.erp.domain.desconto.DescontoSomenteProduto;
import com.ccs.erp.domain.desconto.DescontoSomenteServico;
import com.ccs.erp.domain.entity.Pedido;
import com.ccs.erp.domain.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemService itemService;

    @Transactional(readOnly = true)
    public Page<Pedido> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
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
            return repository.save(pedido);
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
        this.save(pedido);
    }

    @Transactional
    public void abrirPedido(UUID id) {
        var pedido = this.findById(id);
        pedido.abrir();
        this.save(pedido);
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

        //Grava no Banco e retorna
        return this.save(pedido);

    }

    /**
     * <p>Busca no banco os Itens do pedido e seta
     * no {@link com.ccs.erp.domain.entity.Item} do <br>
     * {@link  com.ccs.erp.domain.entity.ItemPedido}</p>
     *
     * @param pedido O pedido que precisa dos itens populados
     */
    private void getItens(Pedido pedido) {

        try {
            pedido.getItensPedido().forEach((itemPedido -> {

                var item = itemService.findById(itemPedido.getItem().getId());

                //O item só pode ser associado a um pedido se estiver ativo
                if (item.getAtivo()) {
                    itemPedido.setItem(item);

                    //seta o pedido no itemPedido
                    //para garantir o relacionamento
                    itemPedido.setPedido(pedido);

                    //seta o valorDesconto como ZERO
                    //para garantir a integridade do banco
                    itemPedido.setValorDesconto(BigDecimal.ZERO);
                }
            }
            ));

        } catch (ItemNaoEncontradoException e) {
            throw new PedidoException(e.getMessage());
        }
    }

    public Pedido aplicarDescontoTodosItens(UUID id, int percentual) {
        var pedido = this.findById(id);

        pedido.aplicarDesconto(percentual, new DescontoItem());

        return this.save(pedido);
    }

    public Pedido aplicarDescontoSomenteProduto(UUID id, Integer percentual) {

        var pedido = this.findById(id);

        pedido.aplicarDesconto(percentual, new DescontoSomenteProduto());

        return this.save(pedido);
    }

    public Pedido aplicarDescontoSomenteServico(UUID id, Integer percentual) {
        var pedido = this.findById(id);

        pedido.aplicarDesconto(percentual, new DescontoSomenteServico());

        return this.save(pedido);
    }
}
