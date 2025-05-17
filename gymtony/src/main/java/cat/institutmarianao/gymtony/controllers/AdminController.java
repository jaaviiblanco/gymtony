package cat.institutmarianao.gymtony.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.model.Responsable;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.UsuarioService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('responsable')")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.getAllUsers());
        model.addAttribute("totalClientes", usuarioService.getAllClientes());
        model.addAttribute("totalMonitores", usuarioService.getAllMonitores());
        model.addAttribute("totalAdmins", usuarioService.getAllResponsables());

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<Usuario> users = usuarioService.getAllUsers();
        model.addAttribute("usuarios", users);
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        Usuario usuario = new Monitor(); 
        model.addAttribute("usuario", usuario);
        return "admin/user-form";
    }



    @PostMapping("/users/new")
    public String createUser(@ModelAttribute Usuario usuario,
                             RedirectAttributes redirectAttributes) {
        if ("monitor".equalsIgnoreCase(usuario.getRole().name())) {
            usuario = new Monitor(usuario.getUsername(), usuario.getPassword(), usuario.getName(), 
                                  usuario.getDni(), usuario.getEmail(), usuario.getAge());
        } else if ("responsable".equalsIgnoreCase(usuario.getRole().name())) {
            usuario = new Responsable(usuario.getUsername(), usuario.getPassword(), usuario.getName(),
                                      usuario.getDni(), usuario.getEmail(), usuario.getAge());
        }

        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
        return "redirect:/admin/users";
    }


    @GetMapping("/users/edit/{dni}")
    public String showEditUserForm(@PathVariable String dni, Model model) {
        Usuario usuario = usuarioService.findByDni(dni)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "admin/user-form";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute Usuario usuario,
                             RedirectAttributes redirectAttributes) {
        usuarioService.update(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
        return "redirect:/admin/users";
    }
}
