package cat.institutmarianao.gymtony.controllers;

import cat.institutmarianao.gymtony.model.Clase;
import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.model.Reserva;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.ClaseService;
import cat.institutmarianao.gymtony.services.ReservaService;
import cat.institutmarianao.gymtony.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clases")
public class ClaseController {

    @Autowired
    private ClaseService claseService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public String listarClases(@RequestParam(required = false) String nombre,
                               @RequestParam(required = false) String monitor,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                               Model model) {

        List<Clase> clases = claseService.findAll();

        if (nombre != null && !nombre.isBlank()) {
            clases = clases.stream()
                    .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .toList();
        }

        if (monitor != null && !monitor.isBlank()) {
            clases = clases.stream()
                    .filter(c -> c.getMonitor() != null &&
                                 c.getMonitor().getName().toLowerCase().contains(monitor.toLowerCase()))
                    .toList();
        }

        if (fecha != null) {
            clases = clases.stream()
                    .filter(c -> c.getFechaHora().toLocalDate().equals(fecha))
                    .toList();
        }

        model.addAttribute("clases", clases);
        return "clases/list";
    }

    @GetMapping("/new")
    public String mostrarFormularioNuevaClase(Model model, Authentication authentication) {
        Clase clase = new Clase();

        boolean esResponsable = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_responsable"));
        model.addAttribute("esResponsable", esResponsable);

        if (esResponsable) {
            clase.setMonitor(new Monitor());
            List<Monitor> monitores = usuarioService.getAllMonitores();
            model.addAttribute("monitores", monitores);
        } else {
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(authentication.getName());
            if (usuarioOpt.isPresent()) {
                clase.setMonitor((Monitor) usuarioOpt.get());
            } else {
                throw new UsernameNotFoundException("Usuario no encontrado: " + authentication.getName());
            }
        }

        model.addAttribute("clase", clase);

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        model.addAttribute("fechaMinima", ahora.format(formatter));

        return "clases/new";
    }

    @PostMapping("/new")
    public String guardarNuevaClase(@Valid @ModelAttribute Clase clase, BindingResult result, Model model, Principal principal, Authentication authentication) {

        boolean esResponsable = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_responsable"));

        if (!esResponsable || clase.getMonitor() == null) {
            Optional<Usuario> optionalUsuario = usuarioService.findByUsername(principal.getName());
            if (optionalUsuario.isPresent() && optionalUsuario.get() instanceof Monitor) {
                clase.setMonitor((Monitor) optionalUsuario.get());
            } else {
                throw new RuntimeException("No se pudo asignar el monitor");
            }
        }

        if (clase.getFechaHora().isBefore(LocalDateTime.now())) {
            result.rejectValue("fechaHora", "error.clase", "La fecha y hora deben ser posteriores a la actual.");
        }

        if (result.hasErrors()) {
            if (esResponsable) {
                List<Monitor> monitores = usuarioService.getAllMonitores();
                model.addAttribute("monitores", monitores);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            model.addAttribute("fechaMinima", LocalDateTime.now().format(formatter));
            model.addAttribute("esResponsable", esResponsable);
            return "clases/new";
        }

        claseService.save(clase);
        return "redirect:/clases";
    }


    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteClase(@PathVariable Long id, Principal principal) {
        String username = principal.getName();

        Optional<Clase> claseOpt = claseService.findById(id);
        if (claseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Clase clase = claseOpt.get();

        if (clase.getMonitor() != null && 
            (clase.getMonitor().getUsername().equals(username) || 
             SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                 .stream().anyMatch(a -> a.getAuthority().equals("ROLE_responsable")))) {
            
            claseService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para eliminar esta clase");
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
        System.out.println("Procesando edición de clase con ID: " + id);
        System.out.println("Datos recibidos: " + clase);
        if (result.hasErrors()) {
            System.out.println("Errores de validación encontrados: " + result.getAllErrors());
            List<Monitor> monitores = usuarioService.getAllMonitores();
            model.addAttribute("monitores", monitores);
            return "clases/edit";
        }
        System.out.println("Validación pasada, buscando clase con ID: " + id);
        Optional<Clase> existingClase = claseService.findById(id);
        if (existingClase.isEmpty()) {
            System.out.println("Clase con ID " + id + " no encontrada");
            return "redirect:/clases";
        }
        try {
            clase.setId(id);
            System.out.println("Guardando clase: " + clase);
            claseService.save(clase);
            System.out.println("Clase guardada con éxito, redirigiendo a /clases");
            return "redirect:/clases";
        } catch (Exception e) {
            System.out.println("Error al guardar la clase: " + e.getMessage());
            List<Monitor> monitores = usuarioService.getAllMonitores();
            model.addAttribute("monitores", monitores);
            model.addAttribute("error", "Error al guardar la clase: " + e.getMessage());
            return "clases/edit";
        }
    }
    
    @GetMapping("/{id}")
    public String detalleClase(@PathVariable Long id, Model model, Principal principal) {
        Optional<Clase> clase = claseService.findById(id);
        if (clase.isEmpty()) {
            return "redirect:/clases";
        }

        model.addAttribute("clase", clase.get());

        boolean yaReservada = false;
        if (principal != null) {
            String username = principal.getName();
            yaReservada = reservaService.estaReservadaPorUsuario(id, username);
        }
        model.addAttribute("yaReservada", yaReservada);

        return "clases/detail";
    }
    
    @PostMapping("/{id}/reservar")
    public String reservarClase(@PathVariable Long id, @AuthenticationPrincipal Cliente cliente, Principal principal) {
        Optional<Clase> claseOpt = claseService.findById(id);
        if (claseOpt.isEmpty()) {
            return "redirect:/clases?error=ClaseNoEncontrada";
        }

        Clase clase = claseOpt.get();

        if (clase.getFechaHora().isBefore(LocalDateTime.now())) {
            return "redirect:/clases?error=ClasePasada";
        }

        if (reservaService.estaReservadaPorUsuario(id, cliente.getUsername())) {
            return "redirect:/clases?error=YaReservada";
        }

        try {
            Reserva reserva = new Reserva();
            reserva.setClase(clase);
            reserva.setCliente(cliente);
            reservaService.save(reserva);
            return "redirect:/clases?reservaExitosa";
        } catch (Exception e) {
            System.out.println("Error al reservar la clase: " + e.getMessage());
            return "redirect:/clases?error=ErrorReserva";
        }
    }

}