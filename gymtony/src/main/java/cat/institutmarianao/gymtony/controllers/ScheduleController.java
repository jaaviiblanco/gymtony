package cat.institutmarianao.gymtony.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cat.institutmarianao.gymtony.services.ClaseService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private final ClaseService claseService;

    public ScheduleController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @GetMapping
    public String showSchedule(
            @RequestParam(value = "date", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {
        
        if (fecha == null) {
            fecha = LocalDate.now();
        }
        
        final LocalDate fechaFinal = fecha; // variable final para la lambda
        
        LocalDate fechaInicio = fechaFinal.with(DayOfWeek.MONDAY);
        LocalDate fechaFin = fechaInicio.plusDays(6);
        LocalDate hoy = LocalDate.now();
        
        // Usamos un Map para los días y sus clases
        Map<String, Object> semana = new LinkedHashMap<>();
        
        for (int i = 0; i < 7; i++) {
            LocalDate diaFecha = fechaInicio.plusDays(i);
            String diaKey = "dia" + i;
            
            semana.put(diaKey, Map.of(
                "fecha", diaFecha,
                "esHoy", diaFecha.equals(hoy),
                "clases", claseService.findClasesByFecha(diaFecha),
                "nombreDia", diaFecha.getDayOfWeek().toString(),
                "fechaFormateada", diaFecha.format(DateTimeFormatter.ofPattern("dd MMM"))
            ));
        }
        
        // Para el día seleccionado (móviles)
        @SuppressWarnings("unchecked")
        Map<String, Object> diaSeleccionado = (Map<String, Object>) semana.values().stream()
                .filter(d -> ((LocalDate) ((Map<?, ?>) d).get("fecha")).equals(fechaFinal))
                .findFirst()
                .orElse((Map<String, Object>) semana.get("dia0"));
        
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("semana", semana);
        model.addAttribute("diaSeleccionado", diaSeleccionado);
        model.addAttribute("hoy", hoy);
        
        return "schedule";
    }

}
