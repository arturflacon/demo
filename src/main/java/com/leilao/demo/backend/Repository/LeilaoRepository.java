package com.leilao.backend.repository;

import com.leilao.backend.enums.StatusLeilao;
import com.leilao.backend.model.Leilao;
import com.leilao.backend.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface LeilaoRepository extends JpaRepository<Leilao, Long> {

    @Query("FROM Leilao l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")

    Long countByStatus(StatusLeilao status);

    Page<Leilao> findByTituloContainingIgnoreCase(@Param("titulo") String titulo, Pageable pageable);

    Page<Leilao> findByStatus(StatusLeilao status, Pageable pageable);

    Page<Leilao> findByVendedor(Pessoa vendedor, Pageable pageable);


    @Query("FROM Leilao l WHERE l.dataHoraFim < :dataAtual AND l.status = :status")
    Page<Leilao> findLeiloesExpirados(@Param("dataAtual") LocalDateTime dataAtual, @Param("status") StatusLeilao status, Pageable pageable);
}
