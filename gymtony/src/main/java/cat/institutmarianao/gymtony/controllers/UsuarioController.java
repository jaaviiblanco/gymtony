package cat.institutmarianao.gymtony.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.model.Responsable;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsers() {
        return ResponseEntity.ok(usuarioService.getAllUsers());
    }

    // Buscar un usuario por username
    @GetMapping("/{username}")
    public ResponseEntity<Usuario> getUserByUsername(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.findByUsername(username);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un usuario
    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @Valid @RequestBody Usuario usuario) {
        Optional<Usuario> existingUser = usuarioService.findByUsername(username);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuario.setId(existingUser.get().getId()); // Mantener el mismo ID
        usuarioService.update(usuario);
        return ResponseEntity.ok("Usuario actualizado correctamente.");
    }

    // Eliminar un usuario
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.findByUsername(username);
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.remove(username);
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, 
                               @RequestParam String name, @RequestParam String dni, 
                               @RequestParam String email, @RequestParam int age, 
                               @RequestParam String role, Model model) {
        if (usuarioService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya está registrado.");
            return "register";
        }

        // Crear el usuario con el role adecuado
        Usuario nuevoUsuario;
        switch (role.toLowerCase()) {
            case "monitor":
                nuevoUsuario = new Monitor(username, password, name, dni, email, age);
                break;
            case "responsable":
                nuevoUsuario = new Responsable(username, password, name, dni, email, age);
                break;
            case "cliente":
            default:
                nuevoUsuario = new Cliente(username, password, name, dni, email, age);
                break;
        }

        // Guardar el usuario en la base de datos
        usuarioService.save(nuevoUsuario);

        // Añadir mensaje de éxito
        model.addAttribute("successMessage", "Usuario creado correctamente.");

        return "redirect:/login";  // Redirigir al login después del registro
    }

}