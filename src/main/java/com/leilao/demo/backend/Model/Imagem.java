package com.leilao.backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "imagem")
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraCadastro = LocalDateTime.now();

    @NotBlank(message = "Nome da imagem é obrigatório")
    private String nomeImagem;

    @Lob
    private byte[] conteudo;

    private String tipoConteudo;

    @ManyToOne
    @JoinColumn(name = "id_leilao")
    @JsonIgnore
    private Leilao leilao;
}
