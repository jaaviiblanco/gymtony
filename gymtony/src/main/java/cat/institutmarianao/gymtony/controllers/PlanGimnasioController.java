package cat.institutmarianao.gymtony.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.PlanGimnasio;
import cat.institutmarianao.gymtony.services.PlanGimnasioService;

@RestController
@RequestMapping("/planes-gimnasio")
public class PlanGimnasioController {

    @Autowired
    private PlanGimnasioService planGimnasioService;

    @GetMapping
    public ResponseEntity<List<PlanGimnasio>> getAllPlanes() {
        List<PlanGimnasio> planes = planGimnasioService.findAll();
        return ResponseEntity.ok(planes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanGimnasio> getPlanById(@PathVariable Long id) {
        Optional<PlanGimnasio> plan = planGimnasioService.findById(id);
        return plan.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlanGimnasio> createPlan(@RequestBody PlanGimnasio plan) {
        PlanGimnasio savedPlan = planGimnasioService.save(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanGimnasio> updatePlan(@PathVariable Long id, @RequestBody PlanGimnasio planDetails) {
        Optional<PlanGimnasio> optionalPlan = planGimnasioService.findById(id);
        if (!optionalPlan.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        PlanGimnasio existingPlan = optionalPlan.get();
        existingPlan.setNombre(planDetails.getNombre());
        existingPlan.setDescripcion(planDetails.getDescripcion());
        existingPlan.setPrecioMensual(planDetails.getPrecioMensual());

        PlanGimnasio updatedPlan = planGimnasioService.save(existingPlan);
        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        if (!planGimnasioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        planGimnasioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
