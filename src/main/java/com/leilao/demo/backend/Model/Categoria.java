package com.leilao.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{validation.name.notblank}")
    private String nome;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "id_pessoa_criador")
    private Pessoa criador;
}
