package com.leilao.backend.dto;

import lombok.Data;

@Data
public class RelatorioDTO {

    private Long totalLeiloes;
    private Long leiloesAbertos;
    private Long leiloesEncerrados;
    private Long totalLances;
    private Double valorTotalArrecadado;
    private Long totalUsuarios;
    private Long totalCategorias;
}
