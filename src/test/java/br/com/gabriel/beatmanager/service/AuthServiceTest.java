package br.com.gabriel.beatmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void cadastrar_deveRetornarAdminQuandoEmailNaoExiste() {
        AdministradorRequestDTO dto = new AdministradorRequestDTO("João", "joao@email.com", "123456");
        Administrador admin = Administrador.builder().id(1L).nome("João").email("joao@email.com").senha("hashed").build();

        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hashed");
        when(administradorRepository.save(any(Administrador.class))).thenReturn(admin);

        AdministradorResponseDTO response = authService.cadastrar(dto);

        assertEquals("João", response.getNome());
        assertEquals("joao@email.com", response.getEmail());
    }

    @Test
    void cadastrar_deveLancarExcecaoQuandoEmailJaExiste() {
        AdministradorRequestDTO dto = new AdministradorRequestDTO("João", "joao@email.com", "123456");
        Administrador adminExistente = Administrador.builder().id(1L).email("joao@email.com").build();

        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(adminExistente));

        assertThrows(EmailJaCadastradoException.class, () -> authService.cadastrar(dto));
    }

    @Test
    void login_deveRetornarTokenQuandoCredenciaisValidas() {
        LoginRequestDTO dto = new LoginRequestDTO("joao@email.com", "123456");
        Administrador admin = Administrador.builder().id(1L).email("joao@email.com").senha("hashed").build();

        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("123456", "hashed")).thenReturn(true);
        when(jwtService.gerarToken(1L, "joao@email.com")).thenReturn("token-jwt");

        AuthResponseDTO response = authService.login(dto);

        assertEquals("token-jwt", response.getToken());
    }

    @Test
    void login_deveLancarExcecaoQuandoEmailNaoExiste() {
        LoginRequestDTO dto = new LoginRequestDTO("inexistente@email.com", "123456");

        when(administradorRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(dto));
    }

    @Test
    void login_deveLancarExcecaoQuandoSenhaIncorreta() {
        LoginRequestDTO dto = new LoginRequestDTO("joao@email.com", "errada");
        Administrador admin = Administrador.builder().id(1L).email("joao@email.com").senha("hashed").build();

        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("errada", "hashed")).thenReturn(false);

        assertThrows(SenhasNaoConferemException.class, () -> authService.login(dto));
    }

    @Test
    void buscarPerfil_deveRetornarAdminQuandoEmailExiste() {
        Administrador admin = Administrador.builder().id(1L).nome("João").email("joao@email.com").senha("hashed").build();

        when(administradorRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(admin));

        AdministradorResponseDTO response = authService.buscarPerfil("joao@email.com");

        assertEquals("João", response.getNome());
        assertEquals("joao@email.com", response.getEmail());
    }

    @Test
    void buscarPerfil_deveLancarExcecaoQuandoEmailNaoExiste() {
        when(administradorRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.buscarPerfil("inexistente@email.com"));
    }
}
