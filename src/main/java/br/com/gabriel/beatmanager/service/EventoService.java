package br.com.gabriel.beatmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.gabriel.beatmanager.dto.request.EventoRequestDTO;
import br.com.gabriel.beatmanager.dto.request.EventoUpdateRequestDTO;
import br.com.gabriel.beatmanager.dto.response.EventoResponseDTO;
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

    public List<EventoResponseDTO> listarTodos() {
        return eventoRepository.findAll().stream()
                .map(EventoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<EventoResponseDTO> listarPorAdministrador(Long administradorId) {
        return eventoRepository.findByAdministradorId(administradorId).stream()
                .map(EventoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public EventoResponseDTO buscarPorId(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
        return EventoResponseDTO.fromEntity(evento);
    }

    public EventoResponseDTO criar(EventoRequestDTO dto) {
        Administrador administrador = administradorRepository.findById(dto.getAdministradorId())
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

        evento.setData(dto.getData());
        evento.setLocalizacao(dto.getLocalizacao());

        return EventoResponseDTO.fromEntity(eventoRepository.save(evento));
    }

    public void deletar(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evento não encontrado");
        }
        eventoRepository.deleteById(id);
    }
}
