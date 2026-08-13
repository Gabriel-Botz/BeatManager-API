package br.com.gabriel.beatmanager.model;

import java.time.LocalDateTime;

import br.com.gabriel.beatmanager.enums.TipoEvento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "evento", schema = "beat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Column(name = "local", nullable = false)
    private String localizacao;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "imagem_url", nullable = false)
    private String imagemUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoEvento tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", nullable = false)
    private Administrador administrador;
}
