package br.com.gabriel.beatmanager.dto.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para atualização de evento (apenas data e localização)")
public class EventoUpdateRequestDTO {

    @NotNull(message = "Data é obrigatória")
    @Schema(description = "Nova data e hora do evento", example = "2026-08-25T20:00:00")
    private LocalDateTime data;

    @NotNull(message = "Localização é obrigatória")
    @Schema(description = "Nova localização do evento", example = "Rua Augusta, 500 - São Paulo")
    private String localizacao;
}
