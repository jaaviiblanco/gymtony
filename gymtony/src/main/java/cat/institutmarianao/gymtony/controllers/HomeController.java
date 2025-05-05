package cat.institutmarianao.gymtony.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Monitor;
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
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            var usuario = usuarioService.findByUsername(username).orElse(null);

            if (usuario != null) {
                model.addAttribute("usuario", usuario);

                if (usuario instanceof Responsable) {
                    model.addAttribute("tipoUsuario", "responsable");
                } else if (usuario instanceof Monitor) {
                    model.addAttribute("tipoUsuario", "monitor");
                } else if (usuario instanceof Cliente) {
                    model.addAttribute("tipoUsuario", "cliente");
                }
            }
        }

        model.addAttribute("reservasConfirmadas", reservaService.findAll());

        model.addAttribute("comentariosRecientes", comentarioService.findAll());

        return "home";
    }

}
