package com.leilao.backend.repository;

import com.leilao.backend.model.Feedback;
import com.leilao.backend.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findByAvaliadoOrderByDataHoraDesc(Pessoa avaliado, Pageable pageable);

    Page<Feedback> findByAvaliadorOrderByDataHoraDesc(Pessoa avaliador, Pageable pageable);

    @Query("SELECT AVG(f.nota) FROM Feedback f WHERE f.avaliado = :pessoa")
    Double findMediaNotasByPessoa(@Param("pessoa") Pessoa pessoa);

    boolean existsByAvaliadorAndAvaliado(Pessoa avaliador, Pessoa avaliado);
}
