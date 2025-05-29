package cat.institutmarianao.gymtony.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comentarios")
public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum TipoComentario {
        clase, monitor
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "clase_id")
    private Clase clase;

    @ManyToOne
    @JoinColumn(name = "monitor_id")
    private Monitor monitor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoComentario tipo;

    @Column(nullable = false, length = 500)
    @NotBlank
    private String texto;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private int calificacion;

    @Column(nullable = false)
    private LocalDateTime fechaComentario;

    public Comentario(Cliente cliente, String texto, int calificacion, TipoComentario tipo) {
        this.cliente = cliente;
        this.texto = texto;
        this.calificacion = calificacion;
        this.tipo = tipo;
        this.fechaComentario = LocalDateTime.now();
    }
}
