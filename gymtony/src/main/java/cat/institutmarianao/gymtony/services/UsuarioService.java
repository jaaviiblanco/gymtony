package cat.institutmarianao.gymtony.services;

import java.util.List;
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
    
    Usuario add(Usuario usuario);
    void update(Usuario usuario);
    void remove(String username);
    
    Usuario findByUsername(String username);
    
    void save(@Valid Usuario usuario);

}
