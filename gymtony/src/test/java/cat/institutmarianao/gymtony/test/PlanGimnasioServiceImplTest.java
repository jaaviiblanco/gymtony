package cat.institutmarianao.gymtony.test;

import cat.institutmarianao.gymtony.model.PlanGimnasio;
import cat.institutmarianao.gymtony.repositories.PlanGimnasioRepository;
import cat.institutmarianao.gymtony.services.impl.PlanGimnasioServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanGimnasioServiceImplTest {

    @Mock
    private PlanGimnasioRepository planGimnasioRepository;

    @InjectMocks
    private PlanGimnasioServiceImpl planGimnasioService;

    private PlanGimnasio plan;

    @BeforeEach
    void setUp() {
        plan = new PlanGimnasio();
        plan.setId(1L);
    }

    @Test
    void testFindAll_ReturnsList() {
        when(planGimnasioRepository.findAll()).thenReturn(List.of(plan));

        List<PlanGimnasio> planes = planGimnasioService.findAll();

        assertNotNull(planes);
        assertEquals(1, planes.size());
        assertTrue(planes.contains(plan));
        verify(planGimnasioRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        when(planGimnasioRepository.findAll()).thenReturn(List.of());

        List<PlanGimnasio> planes = planGimnasioService.findAll();

        assertNotNull(planes);
        assertTrue(planes.isEmpty());
        verify(planGimnasioRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Exists() {
        when(planGimnasioRepository.findById(1L)).thenReturn(Optional.of(plan));

        Optional<PlanGimnasio> found = planGimnasioService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        verify(planGimnasioRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotExists() {
        when(planGimnasioRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<PlanGimnasio> found = planGimnasioService.findById(1L);

        assertFalse(found.isPresent());
        verify(planGimnasioRepository, times(1)).findById(1L);
    }

    @Test
    void testSavePlan() {
        when(planGimnasioRepository.save(plan)).thenReturn(plan);

        PlanGimnasio saved = planGimnasioService.save(plan);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(planGimnasioRepository, times(1)).save(plan);
    }

    @Test
    void testDeleteById() {
        doNothing().when(planGimnasioRepository).deleteById(1L);

        planGimnasioService.deleteById(1L);

        verify(planGimnasioRepository, times(1)).deleteById(1L);
    }
}

