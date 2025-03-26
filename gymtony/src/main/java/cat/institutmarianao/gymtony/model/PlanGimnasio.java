package cat.institutmarianao.gymtony.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "planes_gimnasio")
public class PlanGimnasio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre; // Nombre del plan (Ej: Básico, Premium)

    @Column(nullable = false, length = 255)
    private String descripcion; // Descripción del plan

    @Column(nullable = false)
    private double precioMensual; // Precio mensual del plan

    @Column(nullable = false)
    private boolean activo; // Si el plan está activo o no

    public PlanGimnasio(String nombre, String descripcion, double precioMensual) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioMensual = precioMensual;
        this.activo = true; // Por defecto, los planes están activos
    }
}
