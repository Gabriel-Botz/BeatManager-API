package br.com.gabriel.beatmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta com token JWT e dados do administrador")
public class AuthResponseDTO {

    @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "ID do administrador", example = "1")
    private Long id;

    @Schema(description = "Nome do administrador", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail do administrador", example = "joao@email.com")
    private String email;
}
