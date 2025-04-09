package cat.institutmarianao.gymtony.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.repositories.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario por nombre de usuario
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);

        // Si no se encuentra el usuario, lanzamos una excepción
        Usuario foundUser = usuario.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Devolver un objeto UserDetails con los datos del usuario encontrado
        return User.withUsername(foundUser.getUsername())
                .password(foundUser.getPassword()) // Asegúrate de que la contraseña esté encriptada
                .authorities(foundUser.getAuthorities()) // Obtener las autoridades (roles)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
