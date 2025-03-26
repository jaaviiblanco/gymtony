package cat.institutmarianao.gymtony.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.gymtony.model.PlanGimnasio;

@Repository
public interface PlanGimnasioRepository extends JpaRepository<PlanGimnasio, Long> {
}
