package com.leilao.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "lance")
public class Lance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Valor do lance é obrigatório")
    private Double valorLance;

    private LocalDateTime dataHora = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_pessoa_comprador")
    @NotNull(message = "Comprador é obrigatório")
    private Pessoa comprador;

    @ManyToOne
    @JoinColumn(name = "id_leilao")
    @JsonIgnore
    @NotNull(message = "Leilão é obrigatório")
    private Leilao leilao;
}
