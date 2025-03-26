package cat.institutmarianao.gymtony.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Comentario;

@Service
public interface ComentarioService {

    public List<Comentario> findAll();

    public Optional<Comentario> findById(Long id);

    public Comentario save(Comentario comentario);

    public void deleteById(Long id);

    public List<Comentario> findByClienteId(Long clienteId);

    public List<Comentario> findByCalificacion(int calificacion);
}

