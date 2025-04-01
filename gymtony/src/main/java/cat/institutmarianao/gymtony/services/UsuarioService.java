package cat.institutmarianao.gymtony.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.model.Responsable;
import cat.institutmarianao.gymtony.model.Usuario;
import jakarta.validation.Valid;

public interface UsuarioService extends UserDetailsService {
	List<Usuario> getAllUsers();
    List<Monitor> getAllMonitores();
    List<Responsable> getAllResponsables();
    List<Cliente> getAllClientes();

    void save(@Valid Usuario usuario); // Cambiar add por save

    void update(Usuario usuario);
    void remove(String username);

    Optional<Usuario> findByUsername(String username);
}
