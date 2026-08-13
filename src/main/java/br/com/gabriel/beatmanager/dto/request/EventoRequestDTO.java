package br.com.gabriel.beatmanager.dto.request;

import java.time.LocalDateTime;

import br.com.gabriel.beatmanager.enums.TipoEvento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criação de evento")
public class EventoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do evento", example = "Show de Rock")
    private String nome;

    @NotNull(message = "Data é obrigatória")
    @Schema(description = "Data e hora do evento", example = "2026-08-20T20:00:00")
    private LocalDateTime data;

    @NotBlank(message = "Local é obrigatório")
    @Schema(description = "Localização do evento", example = "Av. Paulista, 1000 - São Paulo")
    private String localizacao;

    @NotBlank(message = "Descrição é obrigatória")
    @Schema(description = "Descrição do evento", example = "Show com as melhores bandas de rock")
    private String descricao;

    @NotBlank(message = "URL da imagem é obrigatória")
    @Schema(description = "URL da imagem do evento", example = "https://r2.example.com/evento.jpg")
    private String imagemUrl;

    @NotNull(message = "Tipo é obrigatório")
    @Schema(description = "Tipo do evento", example = "SHOW")
    private TipoEvento tipo;
}
