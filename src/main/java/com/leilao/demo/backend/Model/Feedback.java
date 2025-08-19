package com.leilao.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Min(value = 1, message = "Nota mínima é 1")
    @Max(value = 5, message = "Nota máxima é 5")
    private Integer nota;

    private LocalDateTime dataHora = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_pessoa_avaliador")
    @NotNull(message = "Avaliador é obrigatório")
    private Pessoa avaliador;

    @ManyToOne
    @JoinColumn(name = "id_pessoa_avaliado")
    @NotNull(message = "Pessoa avaliada é obrigatória")
    private Pessoa avaliado;
}
