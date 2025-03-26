package cat.institutmarianao.gymtony.test;

import cat.institutmarianao.gymtony.model.Horario;
import cat.institutmarianao.gymtony.repositories.HorarioRepository;
import cat.institutmarianao.gymtony.services.impl.HorarioServiceImpl;

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
class HorarioServiceImplTest {

    @Mock
    private HorarioRepository horarioRepository;

    @InjectMocks
    private HorarioServiceImpl horarioService;

    private Horario horario;

    @BeforeEach
    void setUp() {
        horario = new Horario();
        horario.setId(1L);
    }

    @Test
    void testFindAll_ReturnsList() {
        when(horarioRepository.findAll()).thenReturn(List.of(horario));

        List<Horario> horarios = horarioService.findAll();

        assertNotNull(horarios);
        assertEquals(1, horarios.size());
        assertTrue(horarios.contains(horario));
        verify(horarioRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        when(horarioRepository.findAll()).thenReturn(List.of());

        List<Horario> horarios = horarioService.findAll();

        assertNotNull(horarios);
        assertTrue(horarios.isEmpty());
        verify(horarioRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Exists() {
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(horario));

        Optional<Horario> found = horarioService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        verify(horarioRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotExists() {
        when(horarioRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Horario> found = horarioService.findById(1L);

        assertFalse(found.isPresent());
        verify(horarioRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveHorario() {
        when(horarioRepository.save(horario)).thenReturn(horario);

        Horario saved = horarioService.save(horario);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(horarioRepository, times(1)).save(horario);
    }

    @Test
    void testDeleteById() {
        doNothing().when(horarioRepository).deleteById(1L);

        horarioService.deleteById(1L);

        verify(horarioRepository, times(1)).deleteById(1L);
    }
}

