package cat.institutmarianao.gymtony.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cat.institutmarianao.gymtony.model.PlanGimnasio;

@Service
public interface PlanGimnasioService {
	
    public List<PlanGimnasio> findAll();

    public Optional<PlanGimnasio> findById(Long id);

    public PlanGimnasio save(PlanGimnasio plan);

    public void deleteById(Long id);
}