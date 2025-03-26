package cat.institutmarianao.gymtony.test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.model.Responsable;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.model.Usuario.Role;
import cat.institutmarianao.gymtony.repositories.UsuarioRepository;
import cat.institutmarianao.gymtony.services.impl.UsuarioServiceImpl;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Cliente("testuser", "password", "Test User", "12345678A", "test@example.com", 25);
    }

    @Test
    void shouldGetAllUsers() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> usuarios = usuarioService.getAllUsers();

        assertThat(usuarios).isNotEmpty().hasSize(1);
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void shouldGetAllMonitores() {
        Monitor monitor = new Monitor("monitoruser", "password", "Monitor User", "87654321B", "monitor@example.com", 30);
        when(usuarioRepository.findByRole(Role.MONITOR)).thenReturn(List.of(monitor));

        List<Monitor> monitores = usuarioService.getAllMonitores();

        assertThat(monitores).isNotEmpty().hasSize(1);
        verify(usuarioRepository, times(1)).findByRole(Role.MONITOR);
    }

    @Test
    void shouldGetAllResponsables() {
        Responsable responsable = new Responsable("respuser", "password", "Responsable User", "56789012C", "resp@example.com", 40);
        when(usuarioRepository.findByRole(Role.RESPONSABLE)).thenReturn(List.of(responsable));

        List<Responsable> responsables = usuarioService.getAllResponsables();

        assertThat(responsables).isNotEmpty().hasSize(1);
        verify(usuarioRepository, times(1)).findByRole(Role.RESPONSABLE);
    }

    @Test
    void shouldGetAllClientes() {
        when(usuarioRepository.findByRole(Role.CLIENTE)).thenReturn(List.of(usuario));

        List<Cliente> clientes = usuarioService.getAllClientes();

        assertThat(clientes).isNotEmpty().hasSize(1);
        verify(usuarioRepository, times(1)).findByRole(Role.CLIENTE);
    }

    @Test
    void shouldAddUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario savedUser = usuarioService.add(usuario);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getPassword()).isEqualTo("hashedPassword");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void shouldUpdateUserAndEncryptPasswordIfChanged() {
        usuario.setPassword("newPassword");
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newPassword")).thenReturn("hashedNewPassword");

        usuarioService.update(usuario);

        assertThat(usuario.getPassword()).isEqualTo("hashedNewPassword");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        when(usuarioRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.update(usuario))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("Usuario no encontrado para actualización: testuser");
    }

    @Test
    void shouldRemoveUser() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        usuarioService.remove("testuser");

        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentUser() {
        when(usuarioRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.remove("nonexistent"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("Usuario no encontrado: nonexistent");
    }

    @Test
    void shouldLoadUserByUsername() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = usuarioService.loadUserByUsername("testuser");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("testuser");
        verify(usuarioRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByUsername() {
        when(usuarioRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.findByUsername("unknown"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("Usuario no encontrado: unknown");
    }

    @Test
    void shouldSaveUserWithEncodedPassword() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.save(usuario);

        assertThat(usuario.getPassword()).isEqualTo("hashedPassword");
        verify(usuarioRepository, times(1)).save(usuario);
    }
}