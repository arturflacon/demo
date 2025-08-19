package com.leilao.backend.service;

import com.leilao.backend.exception.NaoEncontradoExcecao;
import com.leilao.backend.exception.NegocioExcecao;
import com.leilao.backend.model.Categoria;
import com.leilao.backend.repository.CategoriaRepository;
import com.leilao.backend.security.AuthPessoaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private AuthPessoaProvider authPessoaProvider;

    @Autowired
    private MessageSource messageSource;

    public Categoria inserir(Categoria categoria) {
        validarCategoria(categoria);
        categoria.setCriador(authPessoaProvider.getUsuarioAutenticado());
        return categoriaRepository.save(categoria);
    }

    public Categoria alterar(Categoria categoria) {
        Categoria categoriaBanco = buscarPorId(categoria.getId());
        validarCategoria(categoria);
        
        categoriaBanco.setNome(categoria.getNome());
        categoriaBanco.setObservacao(categoria.getObservacao());
        return categoriaRepository.save(categoriaBanco);
    }

    public void excluir(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoExcecao("Categoria não encontrada com id: " + id));
    }

    public Page<Categoria> buscarTodos(String nome, Pageable pageable) {
        if (StringUtils.hasText(nome)) {
            return categoriaRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return categoriaRepository.findAll(pageable);
    }

    private void validarCategoria(Categoria categoria) {
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            if (categoria.getId() == null) {
                throw new NegocioExcecao("Já existe uma categoria com este nome");
            }
            
            Categoria existente = categoriaRepository.findById(categoria.getId()).orElse(null);
            if (existente != null && !existente.getNome().equals(categoria.getNome())) {
                throw new NegocioExcecao("Já existe uma categoria com este nome");
            }
        }
    }
}
