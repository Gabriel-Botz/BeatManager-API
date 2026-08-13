package br.com.gabriel.beatmanager.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para cadastro/atualização de administrador")
public class AdministradorRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do administrador", example = "João Silva")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "E-mail do administrador", example = "joao@email.com")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do administrador", example = "123456")
    private String senha;
}
