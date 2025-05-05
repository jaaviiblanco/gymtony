package cat.institutmarianao.gymtony.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.gymtony.exception.NotFoundException;
import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Reserva;
import cat.institutmarianao.gymtony.repositories.ClaseRepository;
import cat.institutmarianao.gymtony.repositories.ReservaRepository;
import cat.institutmarianao.gymtony.repositories.UsuarioRepository;
import cat.institutmarianao.gymtony.services.ReservaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    @Override
    public Reserva getById(@NotNull @Positive Long id) {
        return reservaRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Reserva save(@Valid Reserva reserva) {
        return reservaRepository.saveAndFlush(reserva);
    }

    @Override
    public void deleteById(@NotNull Long id) {
        reservaRepository.deleteById(id);
    }
    
    @Override
    public void reservar(@NotNull Long claseId, @NotNull String username) {
        if (estaReservadaPorUsuario(claseId, username)) return;

        var clase = claseRepository.findById(claseId).orElseThrow(NotFoundException::new);
        var usuario = usuarioRepository.findByUsername(username).orElseThrow(NotFoundException::new);

        Reserva reserva = new Reserva();
        reserva.setClase(clase);
        
        if (usuario instanceof Cliente cliente) {
            reserva.setCliente(cliente);
        } else {
            throw new IllegalArgumentException("El usuario debe ser un Cliente para poder reservar.");
        }


        reservaRepository.save(reserva);
    }

    @Override
    public void cancelar(@NotNull Long claseId, @NotNull String username) {
        reservaRepository.findByClaseIdAndClienteUsername(claseId, username)
            .ifPresent(reservaRepository::delete);
    }

    @Override
    public boolean estaReservadaPorUsuario(@NotNull Long claseId, @NotNull String username) {
        return reservaRepository.existsByClaseIdAndClienteUsername(claseId, username);
    }
    
    public List<Reserva> findByCliente(Cliente cliente) {
    	return reservaRepository.findByCliente(cliente);
    }
}

