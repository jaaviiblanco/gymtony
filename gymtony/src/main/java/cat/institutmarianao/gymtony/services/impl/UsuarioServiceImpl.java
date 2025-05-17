package cat.institutmarianao.gymtony.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.model.Responsable;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.repositories.UsuarioRepository;
import cat.institutmarianao.gymtony.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Cliente> getAllClientes() {
        List<Usuario> usuarios = usuarioRepository.findByRole(Usuario.Role.cliente);
        List<Cliente> clientes = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Cliente) {
                clientes.add((Cliente) usuario);
            }
        }
        return clientes;
    }

    @Override
    public List<Monitor> getAllMonitores() {
        List<Usuario> usuarios = usuarioRepository.findByRole(Usuario.Role.monitor);
        List<Monitor> monitores = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Monitor) {
                monitores.add((Monitor) usuario);
            }
        }
        return monitores;
    }

    @Override
    public List<Responsable> getAllResponsables() {
        List<Usuario> usuarios = usuarioRepository.findByRole(Usuario.Role.responsable);
        List<Responsable> responsables = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Responsable) {
                responsables.add((Responsable) usuario);
            }
        }
        return responsables;
    }

    @Override
    public void update(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public void remove(String username) {
        usuarioRepository.deleteByUsername(username);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<Usuario> findByDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public void save(@Valid Usuario usuario) {
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword);
        usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
