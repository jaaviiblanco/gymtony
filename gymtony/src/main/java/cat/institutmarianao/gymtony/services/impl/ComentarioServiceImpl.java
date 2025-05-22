package cat.institutmarianao.gymtony.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Comentario;
import cat.institutmarianao.gymtony.repositories.ComentarioRepository;
import cat.institutmarianao.gymtony.services.ComentarioService;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Override
    public List<Comentario> findAll() {
        return comentarioRepository.findAll();
    }

    @Override
    public Comentario findById(Long id) {
        return comentarioRepository.findById(id).orElse(null);
    }

    @Override
    public Comentario save(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    @Override
    public void deleteById(Long id) {
        comentarioRepository.deleteById(id);
    }

    @Override
    public List<Comentario> findByClienteId(Long clienteId) {
        return comentarioRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Comentario> findByCalificacion(int calificacion) {
        return comentarioRepository.findByCalificacion(calificacion);
    }
    
    @Override
    public List<Comentario> findByMonitorId(Long monitorId) {
        return comentarioRepository.findByMonitorId(monitorId);
    }

    public List<Comentario> findByClienteNameContainingIgnoreCaseAndCalificacion(String nombre, Integer calificacion) {
        return comentarioRepository.findByClienteNameContainingIgnoreCaseAndCalificacion(nombre, calificacion);
    }

	public List<Comentario> findByClienteNameContainingIgnoreCase(String nombre) {
	    return comentarioRepository.findByClienteNameContainingIgnoreCase(nombre);
	}
	
	public List<Comentario> buscarPorUsuarioYCalificacion(String usuario, Integer calificacionInt) {
		return comentarioRepository.buscarPorUsuarioYCalificacion(usuario, calificacionInt);
	}
}