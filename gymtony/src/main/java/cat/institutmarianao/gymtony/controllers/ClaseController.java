package cat.institutmarianao.gymtony.controllers;

import cat.institutmarianao.gymtony.model.Clase;
import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.ClaseService;
import cat.institutmarianao.gymtony.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "clases/list"; // src/main/resources/templates/clases/list.html
    }

    @GetMapping("/{id}")
    public String detalleClase(@PathVariable Long id, Model model) {
        Optional<Clase> clase = claseService.findById(id);
        if (clase.isEmpty()) {
            return "redirect:/clases";
        }
        model.addAttribute("clase", clase.get());
        return "clases/detail"; // src/main/resources/templates/clases/detail.html
    }
    
    @GetMapping("/new")
    public String mostrarFormularioNuevaClase(Model model) {
        Clase clase = new Clase();
        clase.setMonitor(new Monitor()); // 👈 Esto evita el error de binding
        model.addAttribute("clase", clase);

        List<Monitor> monitores = usuarioService.getAllMonitores();
        model.addAttribute("monitores", monitores);

        return "clases/new";
    }


    @PostMapping("/new")
    public String guardarNuevaClase(@ModelAttribute Clase clase) {
        claseService.save(clase);
        return "redirect:/clases";
    }

}
