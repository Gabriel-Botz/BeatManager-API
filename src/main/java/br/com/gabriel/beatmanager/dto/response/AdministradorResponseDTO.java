package br.com.gabriel.beatmanager.dto.response;

import br.com.gabriel.beatmanager.model.Administrador;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta com dados do administrador")
public class AdministradorResponseDTO {

    @Schema(description = "ID do administrador", example = "1")
    private Long id;

    @Schema(description = "Nome do administrador", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail do administrador", example = "joao@email.com")
    private String email;

    public static AdministradorResponseDTO fromEntity(Administrador administrador) {
        AdministradorResponseDTO dto = new AdministradorResponseDTO();
        dto.setId(administrador.getId());
        dto.setNome(administrador.getNome());
        dto.setEmail(administrador.getEmail());
        return dto;
    }
}
