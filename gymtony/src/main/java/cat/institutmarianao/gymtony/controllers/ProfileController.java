package cat.institutmarianao.gymtony.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cat.institutmarianao.gymtony.model.Usuario;
import cat.institutmarianao.gymtony.services.UsuarioService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UsuarioService usuarioService;

    @GetMapping("/edit")
    public String mostrarFormularioEdicion(@AuthenticationPrincipal Usuario usuario, Model model) {
        model.addAttribute("usuario", usuarioService.findByUsername(usuario.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        return "profile/edit";
    }

    @PostMapping("/update")
    public String actualizarPerfil(@AuthenticationPrincipal Usuario usuarioActual,
                                   @RequestParam String name,
                                   @RequestParam String dni,
                                   @RequestParam String email,
                                   @RequestParam int age,
                                   @RequestParam String username,
                                   @RequestParam(required = false) String password,
                                   RedirectAttributes redirectAttributes) {

        if (!usuarioActual.getUsername().equals(username)) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este perfil");
            return "redirect:/profile/edit";
        }

        Usuario usuarioExistente = usuarioService.findByUsername(usuarioActual.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioExistente.setName(name);
        usuarioExistente.setDni(dni);
        usuarioExistente.setEmail(email);
        usuarioExistente.setAge(age);
        usuarioExistente.setUsername(username);
        if (password != null && !password.isBlank()) {
            usuarioExistente.setPassword(password);
        }

        usuarioService.update(usuarioExistente);
        redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente");
        return "redirect:/";
    }

}