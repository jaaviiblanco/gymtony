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

    // Obtener todos los usuarios
    @Override
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Cliente> getAllClientes() {
        // Obtén todos los usuarios con rol CLIENTE
        List<Usuario> usuarios = usuarioRepository.findByRole(Usuario.Role.cliente);
        
        // Filtra la lista para obtener solo los objetos de tipo Cliente
        List<Cliente> clientes = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Cliente) {
                clientes.add((Cliente) usuario);  // Safe casting después de la comprobación
            }
        }
        return clientes;
    }

    @Override
    public List<Monitor> getAllMonitores() {
        // Obtén todos los usuarios con rol MONITOR
        List<Usuario> usuarios = usuarioRepository.findByRole(Usuario.Role.monitor);
        
        // Filtra la lista para obtener solo los objetos de tipo Monitor
        List<Monitor> monitores = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Monitor) {
                monitores.add((Monitor) usuario);  // Safe casting después de la comprobación
            }
        }
        return monitores;
    }

    @Override
    public List<Responsable> getAllResponsables() {
        // Obtén todos los usuarios con rol RESPONSABLE
        List<Usuario> usuarios = usuarioRepository.findByRole(Usuario.Role.responsable);
        
        // Filtra la lista para obtener solo los objetos de tipo Responsable
        List<Responsable> responsables = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Responsable) {
                responsables.add((Responsable) usuario);  // Safe casting después de la comprobación
            }
        }
        return responsables;
    }


    // Actualizar un usuario
    @Override
    public void update(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    // Eliminar un usuario
    @Override
    public void remove(String username) {
        usuarioRepository.deleteByUsername(username);
    }

    // Buscar un usuario por username
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

    // Guardar un nuevo usuario con la contraseña encriptada
    @Override
    public void save(@Valid Usuario usuario) {
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword); // Establece la contraseña encriptada

        // Guarda el usuario en la base de datos
        usuarioRepository.save(usuario);
    }

    // Cargar el usuario por su username para la autenticación
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
