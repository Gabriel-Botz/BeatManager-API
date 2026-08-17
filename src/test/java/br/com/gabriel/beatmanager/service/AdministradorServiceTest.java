package br.com.gabriel.beatmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.gabriel.beatmanager.dto.request.AdministradorRequestDTO;
import br.com.gabriel.beatmanager.dto.response.AdministradorResponseDTO;
import br.com.gabriel.beatmanager.exception.ForbiddenException;
import br.com.gabriel.beatmanager.exception.ResourceNotFoundException;
import br.com.gabriel.beatmanager.model.Administrador;
import br.com.gabriel.beatmanager.repository.AdministradorRepository;

@ExtendWith(MockitoExtension.class)
class AdministradorServiceTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdministradorService administradorService;

    @BeforeEach
    void setUp() {
        Administrador admin = Administrador.builder().id(1L).nome("João").email("joao@email.com").senha("hashed").build();
        autenticarUsuario(admin);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void autenticarUsuario(Administrador admin) {
        UserDetails userDetails = User.builder()
                .username(admin.getEmail())
                .password(admin.getSenha())
                .roles("ADMIN")
                .build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void listarTodos_deveRetornarListaDeAdmins() {
        Administrador admin1 = Administrador.builder().id(1L).nome("João").email("joao@email.com").senha("hash").build();
        Administrador admin2 = Administrador.builder().id(2L).nome("Maria").email("maria@email.com").senha("hash").build();

        when(administradorRepository.findAll()).thenReturn(Arrays.asList(admin1, admin2));

        List<AdministradorResponseDTO> response = administradorService.listarTodos();

        assertEquals(2, response.size());
        assertEquals("João", response.get(0).getNome());
        assertEquals("Maria", response.get(1).getNome());
    }

    @Test
    void buscarPorId_deveRetornarAdminQuandoExiste() {
        Administrador admin = Administrador.builder().id(1L).nome("João").email("joao@email.com").senha("hash").build();

        when(administradorRepository.findById(1L)).thenReturn(Optional.of(admin));

        AdministradorResponseDTO response = administradorService.buscarPorId(1L);

        assertEquals("João", response.getNome());
        assertEquals("joao@email.com", response.getEmail());
    }

    @Test
    void buscarPorId_deveLancarExcecaoQuandoNaoExiste() {
        when(administradorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> administradorService.buscarPorId(99L));
    }

    @Test
    void criar_deveRetornarAdminCriado() {
        AdministradorRequestDTO dto = new AdministradorRequestDTO("João", "joao@email.com", "123456");
        Administrador admin = Administrador.builder().id(1L).nome("João").email("joao@email.com").senha("hashed").build();

        when(passwordEncoder.encode("123456")).thenReturn("hashed");
        when(administradorRepository.save(any(Administrador.class))).thenReturn(admin);

        AdministradorResponseDTO response = administradorService.criar(dto);

        assertEquals("João", response.getNome());
        assertEquals("joao@email.com", response.getEmail());
    }

    @Test
    void atualizar_deveRetornarAdminAtualizado() {
        AdministradorRequestDTO dto = new AdministradorRequestDTO("João Updated", "joao@email.com", "nova123");
        Administrador admin = Administrador.builder().id(1L).nome("João Updated").email("joao@email.com").senha("newHashed").build();

        when(administradorRepository.existsById(1L)).thenReturn(true);
        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(
                Administrador.builder().id(1L).email("joao@email.com").senha("hashed").build()));
        when(passwordEncoder.encode("nova123")).thenReturn("newHashed");
        when(administradorRepository.save(any(Administrador.class))).thenReturn(admin);

        AdministradorResponseDTO response = administradorService.atualizar(1L, dto);

        assertEquals("João Updated", response.getNome());
    }

    @Test
    void atualizar_deveLancarExcecaoQuandoNaoExiste() {
        AdministradorRequestDTO dto = new AdministradorRequestDTO("João", "joao@email.com", "123456");

        when(administradorRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> administradorService.atualizar(99L, dto));
    }

    @Test
    void atualizar_deveLancarExcecaoQuandoNaoEDono() {
        AdministradorRequestDTO dto = new AdministradorRequestDTO("Maria Updated", "maria@email.com", "123456");

        when(administradorRepository.existsById(2L)).thenReturn(true);
        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(
                Administrador.builder().id(1L).email("joao@email.com").senha("hashed").build()));

        assertThrows(ForbiddenException.class, () -> administradorService.atualizar(2L, dto));
    }

    @Test
    void deletar_deveDeletarAdminQuandoExiste() {
        when(administradorRepository.existsById(1L)).thenReturn(true);
        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(
                Administrador.builder().id(1L).email("joao@email.com").senha("hashed").build()));

        administradorService.deletar(1L);

        verify(administradorRepository).deleteById(1L);
    }

    @Test
    void deletar_deveLancarExcecaoQuandoNaoExiste() {
        when(administradorRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> administradorService.deletar(99L));
    }

    @Test
    void deletar_deveLancarExcecaoQuandoNaoEDono() {
        when(administradorRepository.existsById(2L)).thenReturn(true);
        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(
                Administrador.builder().id(1L).email("joao@email.com").senha("hashed").build()));

        assertThrows(ForbiddenException.class, () -> administradorService.deletar(2L));
    }
}
