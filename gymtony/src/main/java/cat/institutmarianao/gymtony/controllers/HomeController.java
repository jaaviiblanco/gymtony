package cat.institutmarianao.gymtony.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cat.institutmarianao.gymtony.model.Responsable;
import cat.institutmarianao.gymtony.services.ComentarioService;
import cat.institutmarianao.gymtony.services.ReservaService;
import cat.institutmarianao.gymtony.services.UsuarioService;

@Controller
public class HomeController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        // Obtener los datos del usuario actual (si está autenticado)
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername(); // Obtiene el nombre de usuario

            // Aquí buscamos el usuario desde tu clase responsable, monitor, cliente, etc.
            Responsable responsable = (Responsable) usuarioService.findByUsername(username).orElse(null);

            if (responsable != null) {
                model.addAttribute("usuario", responsable); // Pasa el objeto responsable a la vista
            }
        }

        // Obtener las reservas confirmadas
        model.addAttribute("reservasConfirmadas", reservaService.findAll()); // Si solo quieres las confirmadas, filtra aquí

        // Obtener los comentarios recientes
        model.addAttribute("comentariosRecientes", comentarioService.findAll()); // Aquí podrías hacer un filtrado por fecha si lo necesitas

        return "home"; // Esta es la vista Thymeleaf
    }
}
