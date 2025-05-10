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
    private Cliente cliente;

    @Column(nullable = false, length = 500)
    @NotBlank
    private String texto; 

    @Column(nullable = false)
    @Min(1) @Max(5)
    private int calificacion;

    @Column(nullable = false)
    private LocalDateTime fechaComentario;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "clase_id", nullable = false)
    private Clase clase;


    public Comentario(Cliente cliente, String texto, int calificacion) {
        this.cliente = cliente;
        this.texto = texto;
        this.calificacion = calificacion;
        this.fechaComentario = LocalDateTime.now();
    }
    
    public void setClienteId(Long clienteId, ClienteRepository clienteRepository) {
        this.cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
}