package br.com.gabriel.beatmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.gabriel.beatmanager.dto.request.AdministradorRequestDTO;
import br.com.gabriel.beatmanager.dto.response.AdministradorResponseDTO;
import br.com.gabriel.beatmanager.model.Administrador;
import br.com.gabriel.beatmanager.repository.AdministradorRepository;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public List<AdministradorResponseDTO> listarTodos() {
        return administradorRepository.findAll().stream()
                .map(AdministradorResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AdministradorResponseDTO buscarPorId(Long id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador não encontrado"));
        return AdministradorResponseDTO.fromEntity(administrador);
    }

    public AdministradorResponseDTO buscarPorEmail(String email) {
        Administrador administrador = administradorRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador não encontrado"));
        return AdministradorResponseDTO.fromEntity(administrador);
    }

    public AdministradorResponseDTO criar(AdministradorRequestDTO dto) {
        Administrador administrador = Administrador.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();
        return AdministradorResponseDTO.fromEntity(administradorRepository.save(administrador));
    }

    public AdministradorResponseDTO atualizar(Long id, AdministradorRequestDTO dto) {
        if (!administradorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador não encontrado");
        }
        Administrador administrador = Administrador.builder()
                .id(id)
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();
        return AdministradorResponseDTO.fromEntity(administradorRepository.save(administrador));
    }

    public void deletar(Long id) {
        if (!administradorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador não encontrado");
        }
        administradorRepository.deleteById(id);
    }
}
