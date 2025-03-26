package cat.institutmarianao.gymtony.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Reserva;
import cat.institutmarianao.gymtony.services.ReservaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/reservas")
@Validated
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable @NotNull @Positive Long id) {
        return ResponseEntity.ok(reservaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Reserva> createReserva(@Valid @RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.save(reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable @NotNull Long id) {
        reservaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
