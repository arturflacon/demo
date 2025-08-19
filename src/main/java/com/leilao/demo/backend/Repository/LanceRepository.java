package com.leilao.backend.repository;

import com.leilao.backend.model.Lance;
import com.leilao.backend.model.Leilao;
import com.leilao.backend.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LanceRepository extends JpaRepository<Lance, Long> {

    Page<Lance> findByLeilaoOrderByDataHoraDesc(Leilao leilao, Pageable pageable);

    Page<Lance> findByCompradorOrderByDataHoraDesc(Pessoa comprador, Pageable pageable);

    @Query("FROM Lance l WHERE l.leilao = :leilao ORDER BY l.valorLance DESC, l.dataHora DESC")
    Optional<Lance> findMaiorLancePorLeilao(@Param("leilao") Leilao leilao);

    @Query("SELECT COUNT(l) FROM Lance l WHERE l.leilao = :leilao")
    Long countLancesByLeilao(@Param("leilao") Leilao leilao);
}
