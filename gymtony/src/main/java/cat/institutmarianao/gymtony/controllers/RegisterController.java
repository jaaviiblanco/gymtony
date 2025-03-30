package cat.institutmarianao.gymtony.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> registerUser(@Valid @RequestBody Usuario usuario) {
        // Comprobar si el usuario ya existe
        try {
            usuarioService.findByUsername(usuario.getUsername());
            return ResponseEntity.badRequest().body("El usuario ya existe.");
        } catch (Exception e) {
            // Si el usuario no existe, lo creamos
            usuarioService.add(usuario);
            return ResponseEntity.ok("Usuario registrado correctamente.");
        }
    }
}
