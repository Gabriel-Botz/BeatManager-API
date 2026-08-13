package br.com.gabriel.beatmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.gabriel.beatmanager.dto.request.EventoRequestDTO;
import br.com.gabriel.beatmanager.dto.request.EventoUpdateRequestDTO;
import br.com.gabriel.beatmanager.dto.response.EventoResponseDTO;
import br.com.gabriel.beatmanager.exception.ForbiddenException;
import br.com.gabriel.beatmanager.exception.ResourceNotFoundException;
import br.com.gabriel.beatmanager.model.Administrador;
import br.com.gabriel.beatmanager.model.Evento;
import br.com.gabriel.beatmanager.repository.AdministradorRepository;
import br.com.gabriel.beatmanager.repository.EventoRepository;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private AdministradorRepository administradorRepository;

    @InjectMocks
    private EventoService eventoService;

    private Administrador administrador;
    private Evento evento;

    @BeforeEach
    void setUp() {
        administrador = Administrador.builder()
                .id(1L)
                .nome("João")
                .email("joao@email.com")
                .senha("hashed")
                .build();

        evento = Evento.builder()
                .id(1L)
                .nome("Show de Rock")
                .data(LocalDateTime.of(2026, 8, 20, 20, 0))
                .localizacao("Av. Paulista, 1000")
                .descricao("Show incrível")
                .imagemUrl("https://example.com/img.jpg")
                .administrador(administrador)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void autenticarUsuario() {
        UserDetails userDetails = User.builder()
                .username("joao@email.com")
                .password("hashed")
                .roles("ADMIN")
                .build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void listarTodos_deveRetornarPaginaDeEventos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Evento> page = new PageImpl<>(java.util.List.of(evento), pageable, 1);

        when(eventoRepository.findAll(pageable)).thenReturn(page);

        Page<EventoResponseDTO> response = eventoService.listarTodos(pageable);

        assertEquals(1, response.getContent().size());
        assertEquals("Show de Rock", response.getContent().get(0).getNome());
    }

    @Test
    void buscarPorId_deveRetornarEventoQuandoExiste() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        EventoResponseDTO response = eventoService.buscarPorId(1L);

        assertEquals("Show de Rock", response.getNome());
    }

    @Test
    void buscarPorId_deveLancarExcecaoQuandoNaoExiste() {
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventoService.buscarPorId(99L));
    }

    @Test
    void criar_deveRetornarEventoCriado() {
        autenticarUsuario();

        EventoRequestDTO dto = new EventoRequestDTO("Show", LocalDateTime.of(2026, 8, 20, 20, 0),
                "Av. Paulista", "Descrição", "https://example.com/img.jpg");

        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(administrador));
        when(administradorRepository.findById(1L)).thenReturn(Optional.of(administrador));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        EventoResponseDTO response = eventoService.criar(dto);

        assertEquals("Show de Rock", response.getNome());
    }

    @Test
    void atualizar_deveRetornarEventoAtualizado() {
        autenticarUsuario();

        EventoUpdateRequestDTO dto = new EventoUpdateRequestDTO(
                LocalDateTime.of(2026, 8, 25, 20, 0), "Rua Augusta, 500");

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(administrador));
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        EventoResponseDTO response = eventoService.atualizar(1L, dto);

        assertEquals("Show de Rock", response.getNome());
    }

    @Test
    void atualizar_deveLancarExcecaoQuandoNaoEDono() {
        autenticarUsuario();

        Administrador outroAdmin = Administrador.builder()
                .id(2L).nome("Maria").email("maria@email.com").senha("hash").build();
        Evento eventoDeOutro = Evento.builder()
                .id(1L).nome("Show").administrador(outroAdmin).build();

        EventoUpdateRequestDTO dto = new EventoUpdateRequestDTO(
                LocalDateTime.of(2026, 8, 25, 20, 0), "Rua Augusta, 500");

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(eventoDeOutro));
        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(administrador));

        assertThrows(ForbiddenException.class, () -> eventoService.atualizar(1L, dto));
    }

    @Test
    void deletar_deveDeletarEventoQuandoEDono() {
        autenticarUsuario();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(administrador));

        eventoService.deletar(1L);
    }

    @Test
    void deletar_deveLancarExcecaoQuandoNaoEDono() {
        autenticarUsuario();

        Administrador outroAdmin = Administrador.builder()
                .id(2L).nome("Maria").email("maria@email.com").senha("hash").build();
        Evento eventoDeOutro = Evento.builder()
                .id(1L).nome("Show").administrador(outroAdmin).build();

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(eventoDeOutro));
        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(administrador));

        assertThrows(ForbiddenException.class, () -> eventoService.deletar(1L));
    }

    @Test
    void deletar_deveLancarExcecaoQuandoNaoExiste() {
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventoService.deletar(99L));
    }
}
