package cat.institutmarianao.gymtony.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cat.institutmarianao.gymtony.model.Clase;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
}
