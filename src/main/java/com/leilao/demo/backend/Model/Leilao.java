package com.leilao.backend.model;

import com.leilao.backend.enums.StatusLeilao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "leilao")
public class Leilao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String descricaoDetalhada;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDateTime dataHoraInicio;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    private StatusLeilao status = StatusLeilao.EM_ANALISE;

    private String observacao;

    @NotNull(message = "Valor de incremento é obrigatório")
    private Double valorIncremento;

    @NotNull(message = "Lance mínimo é obrigatório")
    private Double lanceMinimo;

    @ManyToOne
    @JoinColumn(name = "id_pessoa_vendedor")
    @NotNull(message = "Vendedor é obrigatório")
    private Pessoa vendedor;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    @NotNull(message = "Categoria é obrigatória")
    private Categoria categoria;

    @OneToMany(mappedBy = "leilao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Imagem> imagens;

    @OneToMany(mappedBy = "leilao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lance> lances;

    @OneToOne(mappedBy = "leilao", cascade = CascadeType.ALL)
    private Pagamento pagamento;
}
