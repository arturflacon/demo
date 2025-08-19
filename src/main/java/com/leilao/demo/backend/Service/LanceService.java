package com.leilao.backend.service;

import com.leilao.backend.dto.CriarLanceDTO;
import com.leilao.backend.dto.LanceDTO;
import com.leilao.backend.enums.StatusLeilao;
import com.leilao.backend.exception.NaoEncontradoExcecao;
import com.leilao.backend.exception.NegocioExcecao;
import com.leilao.backend.model.Lance;
import com.leilao.backend.model.Leilao;
import com.leilao.backend.model.Pessoa;
import com.leilao.backend.repository.LanceRepository;
import com.leilao.backend.security.AuthPessoaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LanceService {

    @Autowired
    private LanceRepository lanceRepository;

    @Autowired
    private LeilaoService leilaoService;

    @Autowired
    private AuthPessoaProvider authPessoaProvider;

    public LanceDTO criarLance(CriarLanceDTO criarLanceDTO) {
        Leilao leilao = leilaoService.buscarPorId(criarLanceDTO.getLeilaoId());
        Pessoa comprador = authPessoaProvider.getUsuarioAutenticado();

        validarLance(leilao, criarLanceDTO.getValorLance(), comprador);

        Lance lance = new Lance();
        lance.setLeilao(leilao);
        lance.setComprador(comprador);
        lance.setValorLance(criarLanceDTO.getValorLance());
        lance.setDataHora(LocalDateTime.now());

        lance = lanceRepository.save(lance);
        return converterParaDTO(lance);
    }

    public Page<LanceDTO> buscarLancesPorLeilao(Long leilaoId, Pageable pageable) {
        Leilao leilao = leilaoService.buscarPorId(leilaoId);
        return lanceRepository.findByLeilaoOrderByDataHoraDesc(leilao, pageable)
                .map(this::converterParaDTO);
    }

    public Page<LanceDTO> buscarLancesDoUsuarioLogado(Pageable pageable) {
        Pessoa comprador = authPessoaProvider.getUsuarioAutenticado();
        return lanceRepository.findByCompradorOrderByDataHoraDesc(comprador, pageable)
                .map(this::converterParaDTO);
    }

    public LanceDTO buscarMaiorLancePorLeilao(Long leilaoId) {
        Leilao leilao = leilaoService.buscarPorId(leilaoId);
        Optional<Lance> maiorLance = lanceRepository.findMaiorLancePorLeilao(leilao);

        if (maiorLance.isEmpty()) {
            throw new NaoEncontradoExcecao("Nenhum lance encontrado para este leilão");
        }

        return converterParaDTO(maiorLance.get());
    }

    private void validarLance(Leilao leilao, Double valorLance, Pessoa comprador) {
        // Verificar se leilão está aberto
        if (leilao.getStatus() != StatusLeilao.ABERTO) {
            throw new NegocioExcecao("Leilão não está aberto para lances");
        }

        // Verificar se leilão ainda está dentro do prazo
        if (leilao.getDataHoraFim().isBefore(LocalDateTime.now())) {
            throw new NegocioExcecao("Leilão já foi encerrado");
        }

        // Verificar se não é o próprio vendedor
        if (leilao.getVendedor().getId().equals(comprador.getId())) {
            throw new NegocioExcecao("Vendedor não pode dar lances no próprio leilão");
        }

        // Verificar valor mínimo
        Optional<Lance> maiorLanceAtual = lanceRepository.findMaiorLancePorLeilao(leilao);
        Double valorMinimo = maiorLanceAtual.map(lance -> lance.getValorLance() + leilao.getValorIncremento())
                .orElse(leilao.getLanceMinimo());

        if (valorLance < valorMinimo) {
            throw new NegocioExcecao(String.format("Lance deve ser de pelo menos R$ %.2f", valorMinimo));
        }
    }

    private LanceDTO converterParaDTO(Lance lance) {
        LanceDTO dto = new LanceDTO();
        dto.setId(lance.getId());
        dto.setValorLance(lance.getValorLance());
        dto.setDataHora(lance.getDataHora());
        dto.setNomeComprador(lance.getComprador().getNome());
        dto.setTituloLeilao(lance.getLeilao().getTitulo());
        return dto;
    }
}
