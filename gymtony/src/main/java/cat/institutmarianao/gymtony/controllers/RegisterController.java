package cat.institutmarianao.gymtony.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UsuarioService usuarioService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("usuario", new Cliente("", "", "", "", "", 16));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("usuario") Cliente usuario,
                               BindingResult bindingResult, Model model) {

        // DNI ya existe
        if (usuarioService.findByDni(usuario.getDni()).isPresent()) {
            bindingResult.rejectValue("dni", "error.dni", "Este DNI ya está registrado");
        }

        // Contraseñas no coinciden
        if (!usuario.getPassword().equals(usuario.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Las contraseñas no coinciden");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        usuarioService.save(usuario);
        return "redirect:/login?success=true";
    }
}
