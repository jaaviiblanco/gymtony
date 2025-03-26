package cat.institutmarianao.gymtony.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Clase;
import cat.institutmarianao.gymtony.services.ClaseService;

@RestController
@RequestMapping("/clases")
@Validated
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @GetMapping
    public ResponseEntity<List<Clase>> findAll() {
        List<Clase> clases = claseService.findAll();
        return ResponseEntity.ok(clases);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clase> findById(@PathVariable Long id) {
        Optional<Clase> clase = claseService.findById(id);
        return clase.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Clase> save(@RequestBody Clase clase) {
        Clase savedClase = claseService.save(clase);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clase> update(@PathVariable Long id, @RequestBody Clase clase) {
        if (!claseService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        clase.setId(id);
        Clase updatedClase = claseService.save(clase);
        return ResponseEntity.ok(updatedClase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!claseService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        claseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

