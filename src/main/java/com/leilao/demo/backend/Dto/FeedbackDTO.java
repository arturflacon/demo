package com.leilao.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FeedbackDTO {

    private Long id;
    private String comentario;
    private Integer nota;
    private LocalDateTime dataHora;
    private String nomeAvaliador;
    private String nomeAvaliado;
}
