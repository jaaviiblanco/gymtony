package cat.institutmarianao.gymtony.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.gymtony.model.Monitor;


@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {
}
