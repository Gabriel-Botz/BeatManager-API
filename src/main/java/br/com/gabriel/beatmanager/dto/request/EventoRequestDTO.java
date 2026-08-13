package br.com.gabriel.beatmanager.dto.request;

import java.time.LocalDateTime;

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
public class EventoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Data é obrigatória")
    private LocalDateTime data;

    @NotBlank(message = "Local é obrigatório")
    private String localizacao;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "URL da imagem é obrigatória")
    private String imagemUrl;
}
