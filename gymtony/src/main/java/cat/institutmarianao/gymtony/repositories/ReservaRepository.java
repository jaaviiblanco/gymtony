package cat.institutmarianao.gymtony.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cat.institutmarianao.gymtony.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
