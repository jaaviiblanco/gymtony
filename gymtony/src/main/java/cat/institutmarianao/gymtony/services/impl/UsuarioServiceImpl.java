package cat.institutmarianao.gymtony.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.model.Responsable;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.model.Usuario.Role;
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
    public List<Monitor> getAllMonitores() {
        return usuarioRepository.findByRole(Role.MONITOR).stream()
                .map(usuario -> (Monitor) usuario)
                .collect(Collectors.toList());
    }

    @Override
    public List<Responsable> getAllResponsables() {
        return usuarioRepository.findByRole(Role.RESPONSABLE).stream()
                .map(usuario -> (Responsable) usuario)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> getAllClientes() {
        return usuarioRepository.findByRole(Role.CLIENTE).stream()
                .map(usuario -> (Cliente) usuario)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Usuario usuario) {
        Optional<Usuario> existingUser = usuarioRepository.findById(usuario.getId());

        if (existingUser.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado para actualización: " + usuario.getUsername());
        }

        Usuario userToUpdate = existingUser.get();
        
        // Solo cifrar la contraseña si ha cambiado
        if (!usuario.getPassword().equals(userToUpdate.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        usuarioRepository.save(usuario);
    }

    @Override
    public void remove(String username) {
        Usuario usuario = findByUsername(username);
        usuarioRepository.delete(usuario);
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }

    @Override
    public void save(@Valid Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // Cifra la contraseña antes de guardar
        usuarioRepository.save(usuario);
    }

    @Override
    public Usuario add(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }


	
}
