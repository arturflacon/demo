package com.leilao.backend.controller;

import com.leilao.backend.dto.FeedbackDTO;
import com.leilao.backend.model.Feedback;
import com.leilao.backend.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackDTO> criarFeedback(@Valid @RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.criarFeedback(feedback));
    }

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<Page<FeedbackDTO>> buscarFeedbacksPorPessoa(
            @PathVariable Long pessoaId,
            Pageable pageable) {
        return ResponseEntity.ok(feedbackService.buscarFeedbacksPorPessoa(pessoaId, pageable));
    }

    @GetMapping("/media/{pessoaId}")
    public ResponseEntity<Double> buscarMediaNotas(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(feedbackService.calcularMediaNotas(pessoaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirFeedback(@PathVariable Long id) {
        feedbackService.excluirFeedback(id);
        return ResponseEntity.ok("Feedback excluído com sucesso");
    }
}
