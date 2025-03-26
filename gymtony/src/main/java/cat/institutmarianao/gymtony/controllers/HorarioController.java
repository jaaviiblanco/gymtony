package cat.institutmarianao.gymtony.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Horario;
import cat.institutmarianao.gymtony.services.HorarioService;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping
    public ResponseEntity<List<Horario>> getAllHorarios() {
        List<Horario> horarios = horarioService.findAll();
        return new ResponseEntity<>(horarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horario> getHorarioById(@PathVariable Long id) {
        Optional<Horario> horario = horarioService.findById(id);
        return horario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Horario> createHorario(@RequestBody Horario horario) {
        Horario savedHorario = horarioService.save(horario);
        return new ResponseEntity<>(savedHorario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Horario> updateHorario(@PathVariable Long id, @RequestBody Horario horario) {
        if (!horarioService.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        horario.setId(id);
        Horario updatedHorario = horarioService.save(horario);
        return new ResponseEntity<>(updatedHorario, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHorario(@PathVariable Long id) {
        if (!horarioService.findById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        horarioService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
