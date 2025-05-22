package cat.institutmarianao.gymtony.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.Clase;
import cat.institutmarianao.gymtony.repositories.ClaseRepository;
import cat.institutmarianao.gymtony.services.ClaseService;

@Service
public class ClaseServiceImpl implements ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Override
    public List<Clase> findAll() {
        return claseRepository.findAll();
    }

    @Override
    public Optional<Clase> findById(Long id) {
        return claseRepository.findById(id);
    }

    @Override
    public Clase save(Clase clase) {
        return claseRepository.save(clase);
    }

    @Override
    public void deleteById(Long id) {
    	claseRepository.deleteById(id);
    }

    public List<Clase> findClasesByFecha(LocalDate diaFecha) {
        LocalDateTime inicio = diaFecha.atStartOfDay();
        LocalDateTime fin = diaFecha.atTime(23, 59, 59);
        return claseRepository.findByFechaHoraBetween(inicio, fin);
    }
    
    @Override
    public List<Clase> findByMonitorId(Long monitorId) {
        return claseRepository.findByMonitorId(monitorId);
    }

}


