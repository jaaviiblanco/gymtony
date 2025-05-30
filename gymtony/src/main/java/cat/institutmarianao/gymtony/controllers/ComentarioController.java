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
import cat.institutmarianao.gymtony.model.Comentario.TipoComentario;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.ComentarioService;
import cat.institutmarianao.gymtony.services.ReservaService;
import cat.institutmarianao.gymtony.services.UsuarioService;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ReservaService reservaService;

    @GetMapping("/new")
    public String mostrarFormularioComentario(Model model, @AuthenticationPrincipal Cliente clienteAutenticado) {
        List<Clase> clasesReservadas = reservaService.findClasesReservadasByUsuario(clienteAutenticado.getUsername())
                .stream()
                .filter(clase -> clase.getFechaHora().isBefore(LocalDateTime.now()))
                .toList();

        model.addAttribute("clases", clasesReservadas);
        model.addAttribute("monitores", usuarioService.getAllMonitores());
        model.addAttribute("comentario", new Comentario());
        return "comentarios/formulario";
    }

    @PostMapping("/new")
    public String guardarComentario(@ModelAttribute Comentario comentario,
                                    @AuthenticationPrincipal Cliente clienteAutenticado) {
        comentario.setCliente(clienteAutenticado);
        comentario.setFechaComentario(LocalDateTime.now());

        if (comentario.getMonitor() != null) {
            comentario.setTipo(TipoComentario.monitor);
        } else if (comentario.getClase() != null) {
            comentario.setTipo(TipoComentario.clase);
        } else {
            return "redirect:/comentarios/new?error=SinDestino";
        }

        if (comentario.getClase() != null) {
            // Recupera la clase desde la base de datos
            Clase clase = comentarioService.findClaseById(comentario.getClase().getId());
            if (clase.getFechaHora().isAfter(LocalDateTime.now())) {
                return "redirect:/comentarios/new?error=ClaseNoRealizada";
            }
            if (!reservaService.estaReservadaPorUsuario(clase.getId(), clienteAutenticado.getUsername())) {
                return "redirect:/comentarios/new?error=ClaseNoReservada";
            }
            comentario.setClase(clase); // asegúrate de guardar la clase completa
        }



        comentarioService.save(comentario);
        return "redirect:/comentarios?comentarioCreado";
    }

    @GetMapping
    public String mostrarComentarios(@RequestParam(required = false) String usuario,
                                     @RequestParam(required = false) String calificacion,
                                     Model model) {

        Integer calificacionInt = null;

        if (calificacion != null && !calificacion.equals("_") && !calificacion.isBlank()) {
            try {
                calificacionInt = Integer.parseInt(calificacion);
            } catch (NumberFormatException e) {
                calificacionInt = null;
            }
        }

        List<Comentario> comentariosFiltrados = comentarioService.buscarPorUsuarioYCalificacion(usuario, calificacionInt);

        model.addAttribute("comentarios", comentariosFiltrados);
        model.addAttribute("usuario", usuario);
        model.addAttribute("calificacion", calificacionInt); // esto sirve para mantener el valor seleccionado en el <select>

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