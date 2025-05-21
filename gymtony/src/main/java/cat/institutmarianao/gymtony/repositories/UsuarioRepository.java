package cat.institutmarianao.gymtony.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.model.Usuario.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByDni(String dni);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    
    List<Usuario> findByRole(Role role);

    void deleteByUsername(String username);
}
