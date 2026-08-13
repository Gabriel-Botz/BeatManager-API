package br.com.gabriel.beatmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.beatmanager.dto.request.AdministradorRequestDTO;
import br.com.gabriel.beatmanager.dto.request.LoginRequestDTO;
import br.com.gabriel.beatmanager.dto.response.AdministradorResponseDTO;
import br.com.gabriel.beatmanager.dto.response.AuthResponseDTO;
import br.com.gabriel.beatmanager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Cadastro e login de administradores")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar administrador", description = "Registra um novo administrador no sistema")
    @ApiResponse(responseCode = "201", description = "Administrador cadastrado com sucesso")
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    public AdministradorResponseDTO cadastrar(@RequestBody @Valid AdministradorRequestDTO dto) {
        return authService.cadastrar(dto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica o administrador e retorna um token JWT")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Senha incorreta")
    public AuthResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        return authService.login(dto);
    }

    @GetMapping("/me")
    @Operation(summary = "Perfil do usuário logado", description = "Retorna os dados do administrador autenticado")
    @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso")
    public AdministradorResponseDTO me(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return authService.buscarPerfil(email);
    }
}
