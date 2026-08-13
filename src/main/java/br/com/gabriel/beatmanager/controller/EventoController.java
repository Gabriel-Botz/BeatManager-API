package br.com.gabriel.beatmanager.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import br.com.gabriel.beatmanager.dto.request.EventoUpdateRequestDTO;
import br.com.gabriel.beatmanager.dto.response.EventoResponseDTO;
import br.com.gabriel.beatmanager.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/eventos")
@Tag(name = "Eventos", description = "CRUD de eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos", description = "Lista todos os eventos com paginação")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public Page<EventoResponseDTO> listarTodos(@PageableDefault(size = 10) Pageable pageable) {
        return eventoService.listarTodos(pageable);
    }

    @GetMapping("/administrador/{administradorId}")
    @Operation(summary = "Listar por administrador", description = "Lista eventos de um administrador específico")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public Page<EventoResponseDTO> listarPorAdministrador(@PathVariable Long administradorId,
                                                          @PageableDefault(size = 10) Pageable pageable) {
        return eventoService.listarPorAdministrador(administradorId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Busca um evento pelo ID")
    @ApiResponse(responseCode = "200", description = "Evento encontrado")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    public EventoResponseDTO buscarPorId(@PathVariable Long id) {
        return eventoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar evento", description = "Cria um novo evento. O administrador é extraído do token JWT.")
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    public EventoResponseDTO criar(@RequestBody @Valid EventoRequestDTO dto) {
        return eventoService.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento", description = "Atualiza data e localização de um evento. Apenas o dono pode atualizar.")
    @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso")
    @ApiResponse(responseCode = "403", description = "Sem permissão para alterar este evento")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    public EventoResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid EventoUpdateRequestDTO dto) {
        return eventoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar evento", description = "Remove um evento. Apenas o dono pode deletar.")
    @ApiResponse(responseCode = "204", description = "Evento removido com sucesso")
    @ApiResponse(responseCode = "403", description = "Sem permissão para deletar este evento")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
