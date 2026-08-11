package br.com.gabriel.beatmanager.dto.response;

import br.com.gabriel.beatmanager.model.Administrador;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorResponseDTO {

    private Long id;
    private String nome;
    private String email;

    public static AdministradorResponseDTO fromEntity(Administrador administrador) {
        AdministradorResponseDTO dto = new AdministradorResponseDTO();
        dto.setId(administrador.getId());
        dto.setNome(administrador.getNome());
        dto.setEmail(administrador.getEmail());
        return dto;
    }
}
