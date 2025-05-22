package cat.institutmarianao.gymtony.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.gymtony.model.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
	
    List<Comentario> findByClienteId(Long clienteId);
    
    @Query("SELECT c FROM Comentario c WHERE c.clase.monitor.id = :monitorId")
    List<Comentario> findByMonitorId(Long monitorId);
    
    List<Comentario> findByCalificacion(int calificacion);
    
    List<Comentario> findByClienteNameContainingIgnoreCase(String nombre);

    List<Comentario> findByClienteNameContainingIgnoreCaseAndCalificacion(String nombre, Integer calificacion);
    
    @Query("SELECT c FROM Comentario c WHERE " +
    	       "(:usuario IS NULL OR LOWER(c.cliente.name) LIKE LOWER(CONCAT('%', :usuario, '%'))) AND " +
    	       "(:calificacion IS NULL OR c.calificacion = :calificacion)")
    	List<Comentario> buscarPorUsuarioYCalificacion(@Param("usuario") String usuario,
    	                                               @Param("calificacion") Integer calificacion);

}
