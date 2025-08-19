package com.leilao.backend.service;

import com.leilao.backend.dto.RelatorioDTO;
import com.leilao.backend.enums.StatusLeilao;
import com.leilao.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstatisticasService {

    @Autowired
    private LeilaoRepository leilaoRepository;

    @Autowired
    private LanceRepository lanceRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public RelatorioDTO gerarRelatorioGeral() {
        RelatorioDTO relatorio = new RelatorioDTO();

        relatorio.setTotalLeiloes(leilaoRepository.count());
        relatorio.setLeiloesAbertos(leilaoRepository.countByStatus(StatusLeilao.ABERTO));
        relatorio.setLeiloesEncerrados(leilaoRepository.countByStatus(StatusLeilao.ENCERRADO));
        relatorio.setTotalLances(lanceRepository.count());
        relatorio.setTotalUsuarios(pessoaRepository.count());
        relatorio.setTotalCategorias(categoriaRepository.count());

        // Calcular valor total seria mais complexo, requer agregação
        relatorio.setValorTotalArrecadado(0.0);

        return relatorio;
    }
}
