package cat.institutmarianao.gymtony.model;

import java.io.Serializable;
import java.time.LocalTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "horarios")
public class Horario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private LocalTime horaApertura; // Hora de apertura del gimnasio

    @Column(nullable = false)
    @NotNull
    private LocalTime horaCierre; // Hora de cierre del gimnasio

    public Horario(LocalTime horaApertura, LocalTime horaCierre) {
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }
}
