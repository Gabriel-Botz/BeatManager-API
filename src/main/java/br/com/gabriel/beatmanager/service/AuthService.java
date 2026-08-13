package br.com.gabriel.beatmanager.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gabriel.beatmanager.dto.request.AdministradorRequestDTO;
import br.com.gabriel.beatmanager.dto.request.LoginRequestDTO;
import br.com.gabriel.beatmanager.dto.response.AdministradorResponseDTO;
import br.com.gabriel.beatmanager.dto.response.AuthResponseDTO;
import br.com.gabriel.beatmanager.exception.EmailJaCadastradoException;
import br.com.gabriel.beatmanager.exception.ResourceNotFoundException;
import br.com.gabriel.beatmanager.exception.SenhasNaoConferemException;
import br.com.gabriel.beatmanager.model.Administrador;
import br.com.gabriel.beatmanager.repository.AdministradorRepository;
import br.com.gabriel.beatmanager.security.JwtService;

@Service
public class AuthService {

    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AdministradorRepository administradorRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AdministradorResponseDTO cadastrar(AdministradorRequestDTO dto) {
        if (administradorRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Administrador administrador = Administrador.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .build();

        return AdministradorResponseDTO.fromEntity(administradorRepository.save(administrador));
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        Administrador administrador = administradorRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), administrador.getSenha())) {
            throw new SenhasNaoConferemException("Email ou senha inválidos");
        }

        String token = jwtService.gerarToken(administrador.getId(), administrador.getEmail());
        return new AuthResponseDTO(token, administrador.getId(), administrador.getNome(), administrador.getEmail());
    }
}
