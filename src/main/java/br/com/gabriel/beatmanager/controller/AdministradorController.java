package br.com.gabriel.beatmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.beatmanager.dto.request.AdministradorRequestDTO;
import br.com.gabriel.beatmanager.dto.response.AdministradorResponseDTO;
import br.com.gabriel.beatmanager.service.AdministradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/administradores")
@Tag(name = "Administradores", description = "CRUD de administradores")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping
    @Operation(summary = "Listar todos", description = "Lista todos os administradores cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<AdministradorResponseDTO> listar() {
        return administradorService.listarTodos();
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar por e-mail", description = "Busca um administrador pelo e-mail")
    @ApiResponse(responseCode = "200", description = "Administrador encontrado")
    @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    public AdministradorResponseDTO buscarPorEmail(@PathVariable String email) {
        return administradorService.buscarPorEmail(email);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Busca um administrador pelo ID")
    @ApiResponse(responseCode = "200", description = "Administrador encontrado")
    @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    public AdministradorResponseDTO buscarPorId(@PathVariable Long id) {
        return administradorService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar administrador", description = "Cria um novo administrador")
    @ApiResponse(responseCode = "201", description = "Administrador criado com sucesso")
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    public AdministradorResponseDTO criar(@RequestBody @Valid AdministradorRequestDTO dto) {
        return administradorService.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar administrador", description = "Atualiza os dados de um administrador")
    @ApiResponse(responseCode = "200", description = "Administrador atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    public AdministradorResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid AdministradorRequestDTO dto) {
        return administradorService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar administrador", description = "Remove um administrador pelo ID")
    @ApiResponse(responseCode = "204", description = "Administrador removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        administradorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
