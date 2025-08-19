package com.leilao.backend.service;

import com.leilao.backend.dto.FeedbackDTO;
import com.leilao.backend.exception.NaoEncontradoExcecao;
import com.leilao.backend.exception.NegocioExcecao;
import com.leilao.backend.model.Feedback;
import com.leilao.backend.model.Pessoa;
import com.leilao.backend.repository.FeedbackRepository;
import com.leilao.backend.security.AuthPessoaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private AuthPessoaProvider authPessoaProvider;

    public FeedbackDTO criarFeedback(Feedback feedback) {
        Pessoa avaliador = authPessoaProvider.getUsuarioAutenticado();
        Pessoa avaliado = pessoaService.buscarPorId(feedback.getAvaliado().getId());

        validarFeedback(avaliador, avaliado);

        feedback.setAvaliador(avaliador);
        feedback.setAvaliado(avaliado);
        feedback.setDataHora(LocalDateTime.now());

        feedback = feedbackRepository.save(feedback);
        return converterParaDTO(feedback);
    }

    public Page<FeedbackDTO> buscarFeedbacksPorPessoa(Long pessoaId, Pageable pageable) {
        Pessoa pessoa = pessoaService.buscarPorId(pessoaId);
        return feedbackRepository.findByAvaliadoOrderByDataHoraDesc(pessoa, pageable)
                .map(this::converterParaDTO);
    }

    public Double calcularMediaNotas(Long pessoaId) {
        Pessoa pessoa = pessoaService.buscarPorId(pessoaId);
        Double media = feedbackRepository.findMediaNotasByPessoa(pessoa);
        return media != null ? media : 0.0;
    }

    public void excluirFeedback(Long id) {
        Feedback feedback = buscarPorId(id);
        Pessoa usuarioLogado = authPessoaProvider.getUsuarioAutenticado();

        if (!feedback.getAvaliador().getId().equals(usuarioLogado.getId())) {
            throw new NegocioExcecao("Você só pode excluir seus próprios feedbacks");
        }

        feedbackRepository.delete(feedback);
    }

    public Feedback buscarPorId(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoExcecao("Feedback não encontrado com id: " + id));
    }

    private void validarFeedback(Pessoa avaliador, Pessoa avaliado) {
        if (avaliador.getId().equals(avaliado.getId())) {
            throw new NegocioExcecao("Você não pode avaliar a si mesmo");
        }

        if (feedbackRepository.existsByAvaliadorAndAvaliado(avaliador, avaliado)) {
            throw new NegocioExcecao("Você já avaliou esta pessoa");
        }
    }

    private FeedbackDTO converterParaDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setComentario(feedback.getComentario());
        dto.setNota(feedback.getNota());
        dto.setDataHora(feedback.getDataHora());
        dto.setNomeAvaliador(feedback.getAvaliador().getNome());
        dto.setNomeAvaliado(feedback.getAvaliado().getNome());
        return dto;
    }
}
