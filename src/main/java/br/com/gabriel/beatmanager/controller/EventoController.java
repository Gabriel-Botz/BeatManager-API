package br.com.gabriel.beatmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.gabriel.beatmanager.model.Evento;
import br.com.gabriel.beatmanager.repository.EventoRepository;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoRepository eventoRepository;

    public EventoController(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @GetMapping
    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    @GetMapping("/administrador/{administradorId}")
    public List<Evento> listarPorAdministrador(@PathVariable Long administradorId) {
        return eventoRepository.findByAdministradorId(administradorId);
    }

    @GetMapping("/{id}")
    public Evento buscarPorId(@PathVariable Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Evento criar(@RequestBody Evento evento) {
        return eventoRepository.save(evento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado");
        }
        eventoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}