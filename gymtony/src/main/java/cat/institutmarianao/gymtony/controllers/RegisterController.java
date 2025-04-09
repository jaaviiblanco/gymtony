package cat.institutmarianao.gymtony.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.UsuarioService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("usuario", new Cliente("", "", "", "", "", 16));  
        return "register";  
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password,
                               @RequestParam String confirmPassword, @RequestParam String name,
                               @RequestParam String dni, @RequestParam String email, @RequestParam int age,
                               Model model) {

        // Verificar si el DNI ya existe
        if (usuarioService.findByDni(dni).isPresent()) {
            model.addAttribute("errorDni", true);
            return "register"; 
        }

        // Verificar si las contraseñas coinciden
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorPassword", true);
            return "register"; 
        }

        // Crear un nuevo usuario de tipo Cliente
        Usuario nuevoUsuario = new Cliente(username, password, name, dni, email, age);
        
        // Encriptar la contraseña antes de guardarla
        nuevoUsuario.setPassword(passwordEncoder.encode(password));

        // Guardar el nuevo usuario en la base de datos
        usuarioService.save(nuevoUsuario);

        // Mensaje de éxito
        model.addAttribute("successMessage", "Usuario creado correctamente.");
        
        // Redirigir a la página de login después de crear el usuario.
        return "redirect:/login?success=true";  
    }
}
