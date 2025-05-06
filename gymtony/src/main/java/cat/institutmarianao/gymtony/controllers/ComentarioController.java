package cat.institutmarianao.gymtony.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Comentario;
import cat.institutmarianao.gymtony.services.ComentarioService;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    // Mostrar formulario de nuevo comentario
    @GetMapping("/new")
    public String mostrarFormularioComentario(Model model) {
        model.addAttribute("comentario", new Comentario());
        return "comentarios/formulario";
    }

    // Guardar comentario
    @PostMapping("/new")
    public String guardarComentario(@ModelAttribute Comentario comentario, @AuthenticationPrincipal Cliente clienteAutenticado) {
        comentario.setCliente(clienteAutenticado); 
        comentario.setFechaComentario(LocalDateTime.now());
        comentarioService.save(comentario);
        return "redirect:/comentarios";
    }

    // Mostrar lista de comentarios
    @GetMapping
    public String listarComentarios(Model model) {
        model.addAttribute("comentarios", comentarioService.findAll());
        return "comentarios/lista";
    }
}
