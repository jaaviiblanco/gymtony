package cat.institutmarianao.gymtony.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.repositories.UsuarioRepository;

import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        // Buscar usuario en la base de datos
        Optional<Usuario> optionalUser = usuarioRepository.findByUsername(username);
        
        if (optionalUser.isEmpty()) {
            throw new BadCredentialsException("Usuario no encontrado");
        }

        Usuario user = optionalUser.get();

        // Verificar la contraseña
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        // Si todo está bien, autenticamos al usuario
        return new UsernamePasswordAuthenticationToken(user, rawPassword, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
