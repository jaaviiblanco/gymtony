package cat.institutmarianao.gymtony.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByClaseIdAndClienteUsername(Long claseId, String username);

    boolean existsByClaseIdAndClienteUsername(Long claseId, String username);

	List<Reserva> findByCliente(Cliente cliente);

	List<Reserva> findByClienteUsername(String username);
}
