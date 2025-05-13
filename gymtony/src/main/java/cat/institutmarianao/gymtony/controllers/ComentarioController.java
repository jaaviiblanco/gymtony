package cat.institutmarianao.gymtony.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Clase;
import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Comentario;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.ClaseService;
import cat.institutmarianao.gymtony.services.ComentarioService;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;
    
    @Autowired
    private ClaseService claseService;

    @GetMapping("/new")
    public String mostrarFormularioComentario(Model model) {
        List<Clase> clases = claseService.findAll();
        model.addAttribute("clases", clases); 
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

    @PreAuthorize("hasRole('cliente') or hasRole('responsable')")
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> eliminarComentario(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioAutenticado) {
        Comentario comentario = comentarioService.findById(id);

        if (comentario == null) {
            return ResponseEntity.notFound().build();
        }

        boolean esAutor = comentario.getCliente().equals(usuarioAutenticado);
        boolean esResponsable = usuarioAutenticado.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_responsable"));

        if (esAutor || esResponsable) {
            comentarioService.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(403).build();
    }



}