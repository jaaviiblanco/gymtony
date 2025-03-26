package cat.institutmarianao.gymtony.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.gymtony.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}