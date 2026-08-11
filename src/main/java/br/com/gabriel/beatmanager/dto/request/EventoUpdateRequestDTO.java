package br.com.gabriel.beatmanager.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoUpdateRequestDTO {

    @NotNull(message = "Data é obrigatória")
    private LocalDateTime data;

    @NotNull(message = "Localização é obrigatória")
    private String localizacao;
}
