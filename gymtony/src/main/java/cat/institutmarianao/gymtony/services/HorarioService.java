package cat.institutmarianao.gymtony.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Horario;

@Service
public interface HorarioService {

    public List<Horario> findAll();

    public Optional<Horario> findById(Long id);

    public Horario save(Horario horario);

    public void deleteById(Long id);
}
