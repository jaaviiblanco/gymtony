package cat.institutmarianao.gymtony.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Comentario;
import cat.institutmarianao.gymtony.services.ComentarioService;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public List<Comentario> findAll() {
        return comentarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comentario> findById(@PathVariable Long id) {
        Optional<Comentario> comentario = comentarioService.findById(id);
        return comentario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Comentario save(@RequestBody Comentario comentario) {
        return comentarioService.save(comentario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        comentarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Filtrar comentarios por cliente
    @GetMapping("/cliente/{clienteId}")
    public List<Comentario> findByCliente(@PathVariable Long clienteId) {
        return comentarioService.findByClienteId(clienteId);
    }

    // Filtrar comentarios por calificación
    @GetMapping("/calificacion/{calificacion}")
    public List<Comentario> findByCalificacion(@PathVariable int calificacion) {
        return comentarioService.findByCalificacion(calificacion);
    }
}

