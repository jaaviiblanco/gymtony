package cat.institutmarianao.gymtony.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Clase;
import cat.institutmarianao.gymtony.model.Comentario;

@Service
public interface ComentarioService {

    public List<Comentario> findAll();

    public Comentario findById(Long id);

    public Comentario save(Comentario comentario);

    public void deleteById(Long id);

    public List<Comentario> findByClienteId(Long clienteId);
    public List<Comentario> findByMonitorId(Long monitorId);


    public List<Comentario> findByCalificacion(int calificacion);

	public List<Comentario> findByClienteNameContainingIgnoreCaseAndCalificacion(String usuario, Integer calificacion);

	public List<Comentario> findByClienteNameContainingIgnoreCase(String usuario);

	public List<Comentario> buscarPorUsuarioYCalificacion(String usuario, Integer calificacionInt);

	public Clase findClaseById(Long id);

	public Object findComentariosSobreMonitor(Long id);
}
