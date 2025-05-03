package cat.institutmarianao.gymtony.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clases")
public class Clase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String nombre;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String descripcion;

    @Column(name = "fecha_Hora", nullable = false)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaHora;

    // Relación con Usuario (en lugar de Monitor específico)
    @ManyToOne
    @JoinColumn(name = "monitor_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private Monitor monitor;

    @Column(nullable = false)
    @Min(1) // La clase debe durar al menos 1 minuto
    private int duracion;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String ubicacion;

    public Clase(String nombre, String descripcion, LocalDateTime fechaHora, Monitor monitor, int duracion, String ubicacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
        this.monitor = monitor;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
    }
}
