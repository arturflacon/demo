package com.leilao.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriarLanceDTO {

    @NotNull(message = "ID do leilão é obrigatório")
    private Long leilaoId;

    @NotNull(message = "Valor do lance é obrigatório")
    private Double valorLance;
}
