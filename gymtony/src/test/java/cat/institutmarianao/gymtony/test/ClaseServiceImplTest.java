package cat.institutmarianao.gymtony.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import cat.institutmarianao.gymtony.model.Clase;
import cat.institutmarianao.gymtony.repositories.ClaseRepository;
import cat.institutmarianao.gymtony.services.impl.ClaseServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class ClaseServiceImplTest {

    @Mock
    private ClaseRepository claseRepository;

    @InjectMocks
    private ClaseServiceImpl claseService;

    private Clase clase1;
    private Clase clase2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clase1 = new Clase();
        clase1.setId(1L);
        clase1.setNombre("Yoga");

        clase2 = new Clase();
        clase2.setId(2L);
        clase2.setNombre("CrossFit");
    }

    @Test
    void testFindAll() {
        when(claseRepository.findAll()).thenReturn(Arrays.asList(clase1, clase2));

        List<Clase> clases = claseService.findAll();

        assertNotNull(clases);
        assertEquals(2, clases.size());
        verify(claseRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdExists() {
        when(claseRepository.findById(1L)).thenReturn(Optional.of(clase1));

        Optional<Clase> result = claseService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Yoga", result.get().getNombre());
        verify(claseRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotExists() {
        when(claseRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Clase> result = claseService.findById(99L);

        assertFalse(result.isPresent());
        verify(claseRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveClase() {
        when(claseRepository.save(any(Clase.class))).thenReturn(clase1);

        Clase savedClase = claseService.save(clase1);

        assertNotNull(savedClase);
        assertEquals("Yoga", savedClase.getNombre());
        verify(claseRepository, times(1)).save(clase1);
    }

    @Test
    void testDeleteById() {
        doNothing().when(claseRepository).deleteById(1L);

        claseService.deleteById(1L);

        verify(claseRepository, times(1)).deleteById(1L);
    }
}

