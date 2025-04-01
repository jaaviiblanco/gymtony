package cat.institutmarianao.gymtony.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.UsuarioService;
import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody Usuario usuario) {
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
}
