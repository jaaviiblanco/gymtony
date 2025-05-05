package cat.institutmarianao.gymtony.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Comentario;
import cat.institutmarianao.gymtony.services.ComentarioService;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping("/all")
    public String getComentarios(Model model) {
        model.addAttribute("comentarios", comentarioService.findAll());
        return "comentarios";
    }

    @GetMapping("/cliente/{clienteId}")
    public String getComentariosByCliente(@PathVariable Long clienteId, Model model) {
        model.addAttribute("comentarios", comentarioService.findByClienteId(clienteId));
        return "comentarios";
    }

    @GetMapping("/calificacion/{calificacion}")
    public String getComentariosByCalificacion(@PathVariable int calificacion, Model model) {
        model.addAttribute("comentarios", comentarioService.findByCalificacion(calificacion));
        return "comentarios";
    }

    @GetMapping("/new")
    public String crearComentarioForm(Model model) {
        model.addAttribute("comentario", new Comentario());
        return "resenyas";
    }

    @PostMapping("/new")
    public String crearComentario(@ModelAttribute Comentario comentario) {
        comentarioService.save(comentario);
        return "redirect:/comentarios/all";
    }
}
