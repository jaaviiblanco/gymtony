package cat.institutmarianao.gymtony.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import cat.institutmarianao.gymtony.repositories.ClienteRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comentarios")
public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull
    private Cliente cliente; // Cliente que realiza el comentario

    @Column(nullable = false, length = 500)
    @NotBlank
    private String comentario; // Contenido del comentario

    @Column(nullable = false)
    @Min(1) @Max(5)
    private int calificacion; // Calificación de 1 a 5 estrellas

    @Column(nullable = false)
    private LocalDateTime fechaComentario; // Fecha y hora del comentario

    public Comentario(Cliente cliente, String comentario, int calificacion) {
        this.cliente = cliente;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fechaComentario = LocalDateTime.now();
    }

    // Método auxiliar para establecer solo el cliente ID si es necesario
    public void setClienteId(Long clienteId, ClienteRepository clienteRepository) {
        this.cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
}
