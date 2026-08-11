package br.com.gabriel.beatmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.gabriel.beatmanager.model.Administrador;
import br.com.gabriel.beatmanager.repository.AdministradorRepository;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    private final AdministradorRepository administradorRepository;

    public AdministradorController(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @GetMapping
    public List<Administrador> listar() {
        return administradorRepository.findAll();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Administrador> buscarPorEmail(@PathVariable String email) {
        return administradorRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador não encontrado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> buscarPorId(@PathVariable Long id) {
        return administradorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Administrador criar(@RequestBody Administrador administrador) {
        return administradorRepository.save(administrador);
    }
}