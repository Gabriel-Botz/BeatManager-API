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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping
    public List<AdministradorResponseDTO> listar() {
        return administradorService.listarTodos();
    }

    @GetMapping("/email/{email}")
    public AdministradorResponseDTO buscarPorEmail(@PathVariable String email) {
        return administradorService.buscarPorEmail(email);
    }

    @GetMapping("/{id}")
    public AdministradorResponseDTO buscarPorId(@PathVariable Long id) {
        return administradorService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdministradorResponseDTO criar(@RequestBody @Valid AdministradorRequestDTO dto) {
        return administradorService.criar(dto);
    }

    @PutMapping("/{id}")
    public AdministradorResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid AdministradorRequestDTO dto) {
        return administradorService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        administradorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
