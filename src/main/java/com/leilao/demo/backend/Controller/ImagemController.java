package com.leilao.backend.controller;

import com.leilao.backend.model.Imagem;
import com.leilao.backend.service.ImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/imagem")
public class ImagemController {

    @Autowired
    private ImagemService imagemService;

    @PostMapping("/upload/{leilaoId}")
    public ResponseEntity<Imagem> uploadImagem(
            @PathVariable Long leilaoId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(imagemService.salvarImagem(leilaoId, file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> buscarImagem(@PathVariable Long id) {
        Imagem imagem = imagemService.buscarPorId(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imagem.getTipoConteudo()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + imagem.getNomeImagem() + "\"")
                .body(imagem.getConteudo());
    }

    @GetMapping("/leilao/{leilaoId}")
    public ResponseEntity<List<Imagem>> buscarImagensPorLeilao(@PathVariable Long leilaoId) {
        return ResponseEntity.ok(imagemService.buscarImagensPorLeilao(leilaoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirImagem(@PathVariable Long id) {
        imagemService.excluirImagem(id);
        return ResponseEntity.ok("Imagem excluída com sucesso");
    }
}
