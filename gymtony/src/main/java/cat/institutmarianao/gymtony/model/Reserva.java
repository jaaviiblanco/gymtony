package cat.institutmarianao.gymtony.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Cliente
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Cliente cliente; // Cliente que realiza la reserva

    // Relación con Clase
    @ManyToOne
    @JoinColumn(name = "clase_id", referencedColumnName = "id", nullable = false)
    private Clase clase; // Clase reservada

    @Column(nullable = false)
    private LocalDateTime fechaReserva; // Fecha y hora de la reserva

    @Column(nullable = false)
    private boolean confirmada; // Estado de la reserva (confirmada o no)

    public Reserva(Cliente cliente, Clase clase, LocalDateTime fechaReserva) {
        this.cliente = cliente;
        this.clase = clase;
        this.fechaReserva = fechaReserva;
        this.confirmada = false; // Inicialmente, la reserva no está confirmada
    }
}
