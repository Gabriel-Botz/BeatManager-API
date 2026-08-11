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

import br.com.gabriel.beatmanager.dto.request.EventoRequestDTO;
import br.com.gabriel.beatmanager.dto.response.EventoResponseDTO;
import br.com.gabriel.beatmanager.service.EventoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public List<EventoResponseDTO> listarTodos() {
        return eventoService.listarTodos();
    }

    @GetMapping("/administrador/{administradorId}")
    public List<EventoResponseDTO> listarPorAdministrador(@PathVariable Long administradorId) {
        return eventoService.listarPorAdministrador(administradorId);
    }

    @GetMapping("/{id}")
    public EventoResponseDTO buscarPorId(@PathVariable Long id) {
        return eventoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoResponseDTO criar(@RequestBody @Valid EventoRequestDTO dto) {
        return eventoService.criar(dto);
    }

    @PutMapping("/{id}")
    public EventoResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid EventoRequestDTO dto) {
        return eventoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
