package br.com.gabriel.beatmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.gabriel.beatmanager.dto.request.EventoRequestDTO;
import br.com.gabriel.beatmanager.dto.request.EventoUpdateRequestDTO;
import br.com.gabriel.beatmanager.dto.response.EventoResponseDTO;
import br.com.gabriel.beatmanager.exception.ForbiddenException;
import br.com.gabriel.beatmanager.exception.ResourceNotFoundException;
import br.com.gabriel.beatmanager.model.Administrador;
import br.com.gabriel.beatmanager.model.Evento;
import br.com.gabriel.beatmanager.repository.AdministradorRepository;
import br.com.gabriel.beatmanager.repository.EventoRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final AdministradorRepository administradorRepository;

    public EventoService(EventoRepository eventoRepository, AdministradorRepository administradorRepository) {
        this.eventoRepository = eventoRepository;
        this.administradorRepository = administradorRepository;
    }

    public Page<EventoResponseDTO> listarTodos(Pageable pageable) {
        return eventoRepository.findAll(pageable)
                .map(EventoResponseDTO::fromEntity);
    }

    public Page<EventoResponseDTO> listarPorAdministrador(Long administradorId, Pageable pageable) {
        return eventoRepository.findByAdministradorId(administradorId, pageable)
                .map(EventoResponseDTO::fromEntity);
    }

    public EventoResponseDTO buscarPorId(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
        return EventoResponseDTO.fromEntity(evento);
    }

    public EventoResponseDTO criar(EventoRequestDTO dto) {
        Long administradorId = extrairAdministradorIdDoToken();
        Administrador administrador = administradorRepository.findById(administradorId)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado"));

        Evento evento = Evento.builder()
                .nome(dto.getNome())
                .data(dto.getData())
                .localizacao(dto.getLocalizacao())
                .descricao(dto.getDescricao())
                .imagemUrl(dto.getImagemUrl())
                .administrador(administrador)
                .build();
        return EventoResponseDTO.fromEntity(eventoRepository.save(evento));
    }

    public EventoResponseDTO atualizar(Long id, EventoUpdateRequestDTO dto) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        Long administradorId = extrairAdministradorIdDoToken();
        if (!evento.getAdministrador().getId().equals(administradorId)) {
            throw new ForbiddenException("Você não tem permissão para alterar este evento");
        }

        evento.setData(dto.getData());
        evento.setLocalizacao(dto.getLocalizacao());

        return EventoResponseDTO.fromEntity(eventoRepository.save(evento));
    }

    public void deletar(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        Long administradorId = extrairAdministradorIdDoToken();
        if (!evento.getAdministrador().getId().equals(administradorId)) {
            throw new ForbiddenException("Você não tem permissão para deletar este evento");
        }

        eventoRepository.delete(evento);
    }

    private Long extrairAdministradorIdDoToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails)) {
            throw new ResourceNotFoundException("Usuário não autenticado");
        }
        String email = ((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal()).getUsername();
        return administradorRepository.findByEmail(email)
                .map(Administrador::getId)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador não encontrado"));
    }
}
