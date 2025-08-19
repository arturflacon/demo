package com.leilao.backend.service;

import com.leilao.backend.exception.NaoEncontradoExcecao;
import com.leilao.backend.exception.NegocioExcecao;
import com.leilao.backend.model.Imagem;
import com.leilao.backend.model.Leilao;
import com.leilao.backend.repository.ImagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ImagemService {

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private LeilaoService leilaoService;

    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif"
    );

    private static final long TAMANHO_MAXIMO = 5 * 1024 * 1024; // 5MB

    public Imagem salvarImagem(Long leilaoId, MultipartFile file) {
        Leilao leilao = leilaoService.buscarPorId(leilaoId);
        validarArquivo(file);

        try {
            Imagem imagem = new Imagem();
            imagem.setLeilao(leilao);
            imagem.setNomeImagem(file.getOriginalFilename());
            imagem.setTipoConteudo(file.getContentType());
            imagem.setConteudo(file.getBytes());
            imagem.setDataHoraCadastro(LocalDateTime.now());

            return imagemRepository.save(imagem);
        } catch (IOException e) {
            throw new NegocioExcecao("Erro ao processar imagem: " + e.getMessage());
        }
    }

    public Imagem buscarPorId(Long id) {
        return imagemRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoExcecao("Imagem não encontrada com id: " + id));
    }

    public List<Imagem> buscarImagensPorLeilao(Long leilaoId) {
        Leilao leilao = leilaoService.buscarPorId(leilaoId);
        return imagemRepository.findByLeilao(leilao);
    }

    public void excluirImagem(Long id) {
        Imagem imagem = buscarPorId(id);
        imagemRepository.delete(imagem);
    }

    private void validarArquivo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new NegocioExcecao("Arquivo não pode estar vazio");
        }

        if (file.getSize() > TAMANHO_MAXIMO) {
            throw new NegocioExcecao("Arquivo muito grande. Tamanho máximo: 5MB");
        }

        if (!TIPOS_PERMITIDOS.contains(file.getContentType())) {
            throw new NegocioExcecao("Tipo de arquivo não permitido. Use: JPEG, PNG ou GIF");
        }
    }
}
