package cat.institutmarianao.gymtony.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    // Esta ruta solo será accesible para usuarios con el rol ADMIN
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard"; // Devuelve la vista de administración
    }

    // Ruta para ver todos los usuarios (solo ADMIN)
    @GetMapping("/admin/users")
    public String adminUsers() {
        return "admin/users"; // Devuelve la lista de usuarios
    }
}
