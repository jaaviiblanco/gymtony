package cat.institutmarianao.gymtony.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Reserva;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Service
public interface ReservaService {

    List<Reserva> findAll();

    Reserva getById(@NotNull @Positive Long id);

    Reserva save(@Valid Reserva reserva);

    void deleteById(@NotNull Long id);

    void reservar(@NotNull Long claseId, @NotNull String username);

    void cancelar(@NotNull Long claseId, @NotNull String username);

    boolean estaReservadaPorUsuario(@NotNull Long claseId, @NotNull String username);

    public List<Reserva> findByCliente(Cliente cliente);

}
