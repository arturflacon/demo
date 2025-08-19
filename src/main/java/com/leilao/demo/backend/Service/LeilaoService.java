package com.leilao.backend.service;

import com.leilao.backend.enums.StatusLeilao;
import com.leilao.backend.exception.NaoEncontradoExcecao;
import com.leilao.backend.exception.NegocioExcecao;
import com.leilao.backend.model.Leilao;
import com.leilao.backend.repository.LeilaoRepository;
import com.leilao.backend.security.AuthPessoaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class LeilaoService {

    @Autowired
    private LeilaoRepository leilaoRepository;

    @Autowired
    private AuthPessoaProvider authPessoaProvider;

    public Leilao inserir(Leilao leilao) {
        validarLeilao(leilao);
        leilao.setVendedor(authPessoaProvider.getUsuarioAutenticado());
        leilao.setStatus(StatusLeilao.EM_ANALISE);
        return leilaoRepository.save(leilao);
    }

    public Leilao alterar(Leilao leilao) {
        Leilao leilaoBanco = buscarPorId(leilao.getId());
        validarLeilao(leilao);

        if (leilaoBanco.getStatus() == StatusLeilao.ABERTO) {
            throw new NegocioExcecao("Não é possível alterar leilão em andamento");
        }

        leilaoBanco.setTitulo(leilao.getTitulo());
        leilaoBanco.setDescricao(leilao.getDescricao());
        leilaoBanco.setDescricaoDetalhada(leilao.getDescricaoDetalhada());
        leilaoBanco.setDataHoraInicio(leilao.getDataHoraInicio());
        leilaoBanco.setDataHoraFim(leilao.getDataHoraFim());
        leilaoBanco.setValorIncremento(leilao.getValorIncremento());
        leilaoBanco.setLanceMinimo(leilao.getLanceMinimo());
        leilaoBanco.setCategoria(leilao.getCategoria());
        leilaoBanco.setObservacao(leilao.getObservacao());

        return leilaoRepository.save(leilaoBanco);
    }

    public void excluir(Long id) {
        Leilao leilao = buscarPorId(id);

        if (leilao.getStatus() == StatusLeilao.ABERTO) {
            throw new NegocioExcecao("Não é possível excluir leilão em andamento");
        }

        leilaoRepository.delete(leilao);
    }

    public Leilao buscarPorId(Long id) {
        return leilaoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoExcecao("Leilão não encontrado com id: " + id));
    }

    public Page<Leilao> buscarTodos(String titulo, StatusLeilao status, Pageable pageable) {
        if (StringUtils.hasText(titulo) && status != null) {
            return leilaoRepository.findByTituloContainingIgnoreCase(titulo, pageable);
        } else if (StringUtils.hasText(titulo)) {
            return leilaoRepository.findByTituloContainingIgnoreCase(titulo, pageable);
        } else if (status != null) {
            return leilaoRepository.findByStatus(status, pageable);
        }
        return leilaoRepository.findAll(pageable);
    }

    public Leilao alterarStatus(Long id, StatusLeilao status) {
        Leilao leilao = buscarPorId(id);

        validarMudancaStatus(leilao.getStatus(), status);

        leilao.setStatus(status);
        return leilaoRepository.save(leilao);
    }

    private void validarLeilao(Leilao leilao) {
        if (leilao.getDataHoraInicio().isAfter(leilao.getDataHoraFim())) {
            throw new NegocioExcecao("Data de início não pode ser maior que data de fim");
        }

        if (leilao.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new NegocioExcecao("Data de início não pode ser no passado");
        }

        if (leilao.getValorIncremento() <= 0) {
            throw new NegocioExcecao("Valor de incremento deve ser maior que zero");
        }

        if (leilao.getLanceMinimo() <= 0) {
            throw new NegocioExcecao("Lance mínimo deve ser maior que zero");
        }
    }

    private void validarMudancaStatus(StatusLeilao statusAtual, StatusLeilao novoStatus) {
        switch (statusAtual) {
            case EM_ANALISE:
                if (novoStatus != StatusLeilao.ABERTO && novoStatus != StatusLeilao.CANCELADO) {
                    throw new NegocioExcecao("Leilão em análise só pode ser aberto ou cancelado");
                }
                break;
            case ABERTO:
                if (novoStatus != StatusLeilao.ENCERRADO && novoStatus != StatusLeilao.CANCELADO) {
                    throw new NegocioExcecao("Leilão aberto só pode ser encerrado ou cancelado");
                }
                break;
            case ENCERRADO:
            case CANCELADO:
                throw new NegocioExcecao("Não é possível alterar status de leilão " + statusAtual.name().toLowerCase());
        }
    }
}
