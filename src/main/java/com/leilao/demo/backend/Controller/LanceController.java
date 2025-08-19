package com.leilao.backend.controller;

import com.leilao.backend.dto.CriarLanceDTO;
import com.leilao.backend.dto.LanceDTO;
import com.leilao.backend.service.LanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lance")
public class LanceController {

    @Autowired
    private LanceService lanceService;

    @PostMapping
    public ResponseEntity<LanceDTO> criarLance(@Valid @RequestBody CriarLanceDTO criarLanceDTO) {
        return ResponseEntity.ok(lanceService.criarLance(criarLanceDTO));
    }

    @GetMapping("/leilao/{leilaoId}")
    public ResponseEntity<Page<LanceDTO>> buscarLancesPorLeilao(
            @PathVariable Long leilaoId,
            Pageable pageable) {
        return ResponseEntity.ok(lanceService.buscarLancesPorLeilao(leilaoId, pageable));
    }

    @GetMapping("/meus-lances")
    public ResponseEntity<Page<LanceDTO>> buscarMeusLances(Pageable pageable) {
        return ResponseEntity.ok(lanceService.buscarLancesDoUsuarioLogado(pageable));
    }

    @GetMapping("/maior/{leilaoId}")
    public ResponseEntity<LanceDTO> buscarMaiorLance(@PathVariable Long leilaoId) {
        return ResponseEntity.ok(lanceService.buscarMaiorLancePorLeilao(leilaoId));
    }
}
