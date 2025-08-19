package com.leilao.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.leilao.backend.model.Leilao;
import com.leilao.backend.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByLeilao(Leilao leilao);

    Page<Pagamento> findByStatus(String status, Pageable pageable);
}
