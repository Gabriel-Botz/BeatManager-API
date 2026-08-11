package br.com.gabriel.beatmanager.dto.response;

import java.time.LocalDateTime;

import br.com.gabriel.beatmanager.model.Evento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponseDTO {

    private Long id;
    private String nome;
    private LocalDateTime data;
    private String localizacao;
    private String descricao;
    private String imagemUrl;
    private Long administradorId;
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
