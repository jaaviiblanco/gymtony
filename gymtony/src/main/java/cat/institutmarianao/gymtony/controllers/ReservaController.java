package cat.institutmarianao.gymtony.controllers;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Clase;
import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Reserva;
import cat.institutmarianao.gymtony.services.ClaseService;
import cat.institutmarianao.gymtony.services.ReservaService;
import cat.institutmarianao.gymtony.services.UsuarioService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Controller
@RequestMapping("/reservas")
@Validated
public class ReservaController {

    private final ReservaService reservaService;
    private final ClaseService claseService;
    private final UsuarioService usuarioService;

    public ReservaController(ReservaService reservaService, ClaseService claseService, UsuarioService usuarioService) {
        this.reservaService = reservaService;
        this.claseService = claseService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crear/{claseId}")
    public String reservarClase(@PathVariable @NotNull @Positive Long claseId, Principal principal, Model model) {
        Clase clase = claseService.findById(claseId).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        Cliente cliente = (Cliente) usuarioService.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Reserva reserva = new Reserva(cliente, clase, LocalDateTime.now());
        reservaService.save(reserva);

        return "redirect:/clases?reservaExitosa=true";
    }



    @PostMapping("/cancelar/{claseId}")
    public String cancelarReserva(@PathVariable @NotNull @Positive Long claseId, Principal principal) {
        String username = principal.getName();
        reservaService.cancelar(claseId, username);
        return "redirect:/clases?reservaCancelada=true";
    }

}
