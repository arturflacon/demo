package com.leilao.backend.controller;

import com.leilao.backend.enums.StatusLeilao;
import com.leilao.backend.model.Leilao;
import com.leilao.backend.service.LeilaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leilao")
public class LeilaoController {

    @Autowired
    private LeilaoService leilaoService;

    @GetMapping
    public ResponseEntity<Page<Leilao>> buscarTodos(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) StatusLeilao status,
            Pageable pageable) {
        return ResponseEntity.ok(leilaoService.buscarTodos(titulo, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leilao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(leilaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Leilao> inserir(@Valid @RequestBody Leilao leilao) {
        return ResponseEntity.ok(leilaoService.inserir(leilao));
    }

    @PutMapping
    public ResponseEntity<Leilao> alterar(@Valid @RequestBody Leilao leilao) {
        return ResponseEntity.ok(leilaoService.alterar(leilao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        leilaoService.excluir(id);
        return ResponseEntity.ok("Leilão excluído com sucesso");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Leilao> alterarStatus(
            @PathVariable Long id,
            @RequestParam StatusLeilao status) {
        return ResponseEntity.ok(leilaoService.alterarStatus(id, status));
    }
}
