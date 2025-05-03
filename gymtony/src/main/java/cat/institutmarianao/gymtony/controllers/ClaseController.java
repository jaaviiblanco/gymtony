package cat.institutmarianao.gymtony.controllers;

import cat.institutmarianao.gymtony.model.Clase;
import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.services.ClaseService;
import cat.institutmarianao.gymtony.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clases")
public class ClaseController {

    @Autowired
    private ClaseService claseService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarClases(Model model) {
        List<Clase> clases = claseService.findAll();
        model.addAttribute("clases", clases);
        return "clases/list";
    }

    @GetMapping("/{id}")
    public String detalleClase(@PathVariable Long id, Model model) {
        Optional<Clase> clase = claseService.findById(id);
        if (clase.isEmpty()) {
            return "redirect:/clases";
        }
        model.addAttribute("clase", clase.get());
        return "clases/detail";
    }
    
    @GetMapping("/new")
    public String mostrarFormularioNuevaClase(Model model) {
        Clase clase = new Clase();
        clase.setMonitor(new Monitor());
        model.addAttribute("clase", clase);
        List<Monitor> monitores = usuarioService.getAllMonitores();
        model.addAttribute("monitores", monitores);
        return "clases/new";
    }

    @PostMapping("/new")
    public String guardarNuevaClase(@Valid @ModelAttribute Clase clase, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Monitor> monitores = usuarioService.getAllMonitores();
            model.addAttribute("monitores", monitores);
            return "clases/new";
        }
        System.out.println("Fecha recibida: " + clase.getFechaHora()); 
        claseService.save(clase);
        return "redirect:/clases";
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteClase(@PathVariable Long id) {
        claseService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEditarClase(@PathVariable Long id, Model model) {
        Optional<Clase> clase = claseService.findById(id);
        if (clase.isEmpty()) {
            return "redirect:/clases";
        }
        model.addAttribute("clase", clase.get());
        List<Monitor> monitores = usuarioService.getAllMonitores();
        model.addAttribute("monitores", monitores);
        return "clases/edit";
    }

    @PostMapping("/edit/{id}")
    public String guardarClaseEditada(@PathVariable Long id, @Valid @ModelAttribute Clase clase, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Monitor> monitores = usuarioService.getAllMonitores();
            model.addAttribute("monitores", monitores);
            return "clases/edit";
        }
        Optional<Clase> existingClase = claseService.findById(id);
        if (existingClase.isEmpty()) {
            return "redirect:/clases";
        }
        clase.setId(id);
        claseService.save(clase);
        return "redirect:/clases/" + id;
    }
}