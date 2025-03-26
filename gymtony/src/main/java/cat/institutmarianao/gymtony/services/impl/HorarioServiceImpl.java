package cat.institutmarianao.gymtony.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Horario;
import cat.institutmarianao.gymtony.repositories.HorarioRepository;
import cat.institutmarianao.gymtony.services.HorarioService;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    @Override
    public Optional<Horario> findById(Long id) {
        return horarioRepository.findById(id);
    }

    @Override
    public Horario save(Horario horario) {
        return horarioRepository.save(horario);
    }

    @Override
    public void deleteById(Long id) {
    	horarioRepository.deleteById(id);
    }
}


