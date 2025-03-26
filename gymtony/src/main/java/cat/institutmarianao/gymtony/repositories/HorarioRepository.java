package cat.institutmarianao.gymtony.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.gymtony.model.Horario;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
}
