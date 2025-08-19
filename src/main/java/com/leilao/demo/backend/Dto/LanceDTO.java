package com.leilao.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LanceDTO {

    private Long id;
    private Double valorLance;
    private LocalDateTime dataHora;
    private String nomeComprador;
    private String tituloLeilao;
}
