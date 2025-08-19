package com.leilao.backend.model;

import com.leilao.backend.enums.TipoPerfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{validation.name.notblank}")
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoPerfil tipo;
}
