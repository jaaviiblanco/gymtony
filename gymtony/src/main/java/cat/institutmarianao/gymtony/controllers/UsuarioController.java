package cat.institutmarianao.gymtony.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
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
        Usuario usuario = (Usuario) usuarioService.findByUsername(username);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    // Actualizar un usuario
    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @Valid @RequestBody Usuario usuario) {
        Usuario existingUser = (Usuario) usuarioService.findByUsername(username);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        usuario.setId(existingUser.getId()); // Mantener el mismo ID
        usuarioService.update(usuario);
        return ResponseEntity.ok("Usuario actualizado correctamente.");
    }

    // Eliminar un usuario
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        Usuario usuario = (Usuario) usuarioService.findByUsername(username);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.remove(username);
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }
}
