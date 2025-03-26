package cat.institutmarianao.gymtony.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cat.institutmarianao.gymtony.model.Responsable;

@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Long> {
}
