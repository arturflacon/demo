package com.leilao.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leilao.backend.model.Imagem;
import com.leilao.backend.model.Leilao;

public interface ImagemRepository extends JpaRepository<Imagem, Long> {

    List<Imagem> findByLeilao(Leilao leilao);

    void deleteByLeilao(Leilao leilao);
}
