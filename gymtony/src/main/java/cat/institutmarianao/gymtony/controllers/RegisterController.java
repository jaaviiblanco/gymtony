package cat.institutmarianao.gymtony.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.services.UsuarioService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("usuario", new Cliente());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, Model model) {
        if (usuarioService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El usuario ya existe.");
            return "register";
        }

        Cliente nuevoUsuario = new Cliente();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPassword(passwordEncoder.encode(password)); // Cifrar contraseña
        usuarioService.save(nuevoUsuario);
        
        model.addAttribute("successMessage", "usuario.creado"); // Mensaje de éxito

        return "redirect:/login";
    }
}
