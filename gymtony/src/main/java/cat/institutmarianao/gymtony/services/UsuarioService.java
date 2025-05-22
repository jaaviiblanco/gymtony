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

    void save(@Valid Usuario usuario);

    void update(Usuario usuario);
    void remove(String username);

    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByDni(String dni);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);

    
    public List<Usuario> filterUsers(String role, String search);
	void deleteById(Long id);
}
