package cat.institutmarianao.gymtony.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cat.institutmarianao.gymtony.model.Cliente;
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
                             @RequestParam String confirmPassword,
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

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/admin/users/new";
        }

        if (password.length() < 8) {
            redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 8 caracteres");
            return "redirect:/admin/users/new";
        }

        Usuario usuario;
        if (role == Usuario.Role.monitor) {
            usuario = new Monitor(username, password, name, dni, email, age);
        } else if (role == Usuario.Role.responsable) {
            usuario = new Responsable(username, password, name, dni, email, age);
        } else if (role == Usuario.Role.cliente) {
            usuario = new Cliente(username, password, name, dni, email, age);
        } else {
            throw new IllegalArgumentException("Rol no válido");
        }

        try {
            usuarioService.save(usuario);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Nombre de usuario, email o DNI ya están registrados");
            return "redirect:/admin/users/new";
        }

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
                             @RequestParam(required = false) String password,
                             @RequestParam(required = false) String confirmPassword,
                             @RequestParam Usuario.Role role,
                             RedirectAttributes redirectAttributes) {

        Usuario usuarioExistente = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!username.equals(usuarioExistente.getUsername()) && usuarioService.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya está registrado");
            return "redirect:/admin/users/edit/" + id;
        }
        if (!email.equals(usuarioExistente.getEmail()) && usuarioService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "El email ya está registrado");
            return "redirect:/admin/users/edit/" + id;
        }
        if (!dni.equals(usuarioExistente.getDni()) && usuarioService.existsByDni(dni)) {
            redirectAttributes.addFlashAttribute("error", "El DNI ya está registrado");
            return "redirect:/admin/users/edit/" + id;
        }

        String passwordToUse = usuarioExistente.getPassword();
        if (password != null && !password.isBlank()) {
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                return "redirect:/admin/users/edit/" + id;
            }
            if (password.length() < 8) {
                redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 8 caracteres");
                return "redirect:/admin/users/edit/" + id;
            }
            passwordToUse = password;
        }

        Usuario usuarioActualizado;
        if (usuarioExistente.getRole() != role) {
            if (role == Usuario.Role.monitor) {
                usuarioActualizado = new Monitor();
            } else if (role == Usuario.Role.responsable) {
                usuarioActualizado = new Responsable();
            } else if (role == Usuario.Role.cliente) {
                usuarioActualizado = new Cliente();
            } else {
                throw new IllegalArgumentException("Rol no válido");
            }
            usuarioActualizado.setId(id);
        } else {
            usuarioActualizado = usuarioExistente; 
        }


        usuarioActualizado.setUsername(username);
        usuarioActualizado.setPassword(passwordToUse);
        usuarioActualizado.setName(name);
        usuarioActualizado.setDni(dni);
        usuarioActualizado.setEmail(email);
        usuarioActualizado.setAge(age);

        try {
            usuarioService.saveWithRoleChange(usuarioExistente, usuarioActualizado);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Error: el nombre de usuario, email o DNI ya están registrados");
            return "redirect:/admin/users/edit/" + id;
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        usuarioService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
        return "redirect:/admin/users";
    }
}
