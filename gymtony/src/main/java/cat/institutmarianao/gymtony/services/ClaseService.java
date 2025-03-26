package cat.institutmarianao.gymtony.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Clase;

@Service
public interface ClaseService {

    public List<Clase> findAll();

    public Optional<Clase> findById(Long id);

    public Clase save(Clase clase);

    public void deleteById(Long id);
}
