package cat.institutmarianao.gymtony.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.PlanGimnasio;
import cat.institutmarianao.gymtony.repositories.PlanGimnasioRepository;
import cat.institutmarianao.gymtony.services.PlanGimnasioService;

@Service
public class PlanGimnasioServiceImpl implements PlanGimnasioService {

    @Autowired
    private PlanGimnasioRepository planGimnasioRepository;

    @Override
    public List<PlanGimnasio> findAll() {
        return planGimnasioRepository.findAll();
    }

    @Override
    public Optional<PlanGimnasio> findById(Long id) {
        return planGimnasioRepository.findById(id);
    }

    @Override
    public PlanGimnasio save(PlanGimnasio plan) {
        return planGimnasioRepository.save(plan);
    }

    @Override
    public void deleteById(Long id) {
        planGimnasioRepository.deleteById(id);
    }
}

