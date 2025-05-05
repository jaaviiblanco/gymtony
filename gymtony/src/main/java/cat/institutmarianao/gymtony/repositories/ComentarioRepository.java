package cat.institutmarianao.gymtony.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.gymtony.model.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
	
    List<Comentario> findByClienteId(Long clienteId);
    
    List<Comentario> findByCalificacion(int calificacion);
}
