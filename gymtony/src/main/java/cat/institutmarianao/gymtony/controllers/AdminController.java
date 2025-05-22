package cat.institutmarianao.gymtony.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import cat.institutmarianao.gymtony.model.Monitor;
import cat.institutmarianao.gymtony.model.Responsable;
import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String listUsers(@RequestParam(required = false) String role,
                            @RequestParam(required = false) String search,
                            Model model) {

        List<Usuario> users = (role == null || role.isBlank()) && (search == null || search.isBlank())
                ? usuarioService.getAllUsers()
                : usuarioService.filterUsers(role, search);

        model.addAttribute("usuarios", users);
        model.addAttribute("selectedRole", role);
        model.addAttribute("searchTerm", search);

        return "admin/users";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("usuario", new Monitor("", "", "", "", "", 16));
        model.addAttribute("roles", Usuario.Role.values());
        return "admin/user-form";
    }

    @PostMapping("/users/new")
    public String createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String name,
                             @RequestParam String dni,
                             @RequestParam String email,
                             @RequestParam int age,
                             @RequestParam Usuario.Role role,
                             RedirectAttributes redirectAttributes) {

        if (password == null || password.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "La contraseña es obligatoria");
            return "redirect:/admin/users/new";
        }

        if (password.length() < 10) {
            redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 10 caracteres");
            return "redirect:/admin/users/new";
        }

        Usuario usuario;
        if (role == Usuario.Role.monitor) {
            usuario = new Monitor(username, password, name, dni, email, age);
        } else if (role == Usuario.Role.responsable) {
            usuario = new Responsable(username, password, name, dni, email, age);
        } else {
            throw new IllegalArgumentException("Rol no válido");
        }

        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Usuario.Role.values());
        return "admin/user-form";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @RequestParam String username,
                             @RequestParam String name,
                             @RequestParam String dni,
                             @RequestParam String email,
                             @RequestParam int age,
                             @RequestParam String password,
                             @RequestParam Usuario.Role role,
                             RedirectAttributes redirectAttributes) {

        Usuario usuarioExistente = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuarioExistente.setUsername(username);
        usuarioExistente.setName(name);
        usuarioExistente.setDni(dni);
        usuarioExistente.setEmail(email);
        usuarioExistente.setAge(age);
        usuarioExistente.setRole(role);

        if (password != null && !password.isBlank()) {
            usuarioExistente.setPassword(password);
        }

        usuarioService.update(usuarioExistente);
        redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente");

        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        usuarioService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
        return "redirect:/admin/users";
    }
}
