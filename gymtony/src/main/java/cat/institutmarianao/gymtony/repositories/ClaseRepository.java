package cat.institutmarianao.gymtony.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cat.institutmarianao.gymtony.model.Clase;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

	List<Clase> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}
