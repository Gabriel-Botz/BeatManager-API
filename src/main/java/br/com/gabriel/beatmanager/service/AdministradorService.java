package br.com.gabriel.beatmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gabriel.beatmanager.dto.request.AdministradorRequestDTO;
import br.com.gabriel.beatmanager.dto.response.AdministradorResponseDTO;
import br.com.gabriel.beatmanager.exception.ResourceNotFoundException;
import br.com.gabriel.beatmanager.model.Administrador;
import br.com.gabriel.beatmanager.repository.AdministradorRepository;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministradorService(AdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AdministradorResponseDTO> listarTodos() {
        return administradorRepository.findAll().stream()
                .map(AdministradorResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AdministradorResponseDTO buscarPorId(Long id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado"));
        return AdministradorResponseDTO.fromEntity(administrador);
    }

    public AdministradorResponseDTO buscarPorEmail(String email) {
        Administrador administrador = administradorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado"));
        return AdministradorResponseDTO.fromEntity(administrador);
    }

    public AdministradorResponseDTO criar(AdministradorRequestDTO dto) {
        Administrador administrador = Administrador.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .build();
        return AdministradorResponseDTO.fromEntity(administradorRepository.save(administrador));
    }

    public AdministradorResponseDTO atualizar(Long id, AdministradorRequestDTO dto) {
        if (!administradorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Administrador não encontrado");
        }
        Administrador administrador = Administrador.builder()
                .id(id)
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .build();
        return AdministradorResponseDTO.fromEntity(administradorRepository.save(administrador));
    }

    public void deletar(Long id) {
        if (!administradorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Administrador não encontrado");
        }
        administradorRepository.deleteById(id);
    }
}
