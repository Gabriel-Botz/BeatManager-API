package br.com.gabriel.beatmanager.dto.response;

import java.time.LocalDateTime;

import br.com.gabriel.beatmanager.model.Evento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta com dados do evento")
public class EventoResponseDTO {

    @Schema(description = "ID do evento", example = "1")
    private Long id;

    @Schema(description = "Nome do evento", example = "Show de Rock")
    private String nome;

    @Schema(description = "Data e hora do evento", example = "2026-08-20T20:00:00")
    private LocalDateTime data;

    @Schema(description = "Localização do evento", example = "Av. Paulista, 1000 - São Paulo")
    private String localizacao;

    @Schema(description = "Descrição do evento", example = "Show com as melhores bandas de rock")
    private String descricao;

    @Schema(description = "URL da imagem do evento", example = "https://r2.example.com/evento.jpg")
    private String imagemUrl;

    @Schema(description = "ID do administrador responsável", example = "1")
    private Long administradorId;

    @Schema(description = "Nome do administrador responsável", example = "João Silva")
    private String administradorNome;

    public static EventoResponseDTO fromEntity(Evento evento) {
        EventoResponseDTO dto = new EventoResponseDTO();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setData(evento.getData());
        dto.setLocalizacao(evento.getLocalizacao());
        dto.setDescricao(evento.getDescricao());
        dto.setImagemUrl(evento.getImagemUrl());
        if (evento.getAdministrador() != null) {
            dto.setAdministradorId(evento.getAdministrador().getId());
            dto.setAdministradorNome(evento.getAdministrador().getNome());
        }
        return dto;
    }
}
