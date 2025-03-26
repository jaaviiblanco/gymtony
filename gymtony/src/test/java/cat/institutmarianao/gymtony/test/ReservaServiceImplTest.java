package cat.institutmarianao.gymtony.test;

import cat.institutmarianao.gymtony.exception.NotFoundException;
import cat.institutmarianao.gymtony.model.Reserva;
import cat.institutmarianao.gymtony.repositories.ReservaRepository;
import cat.institutmarianao.gymtony.services.impl.ReservaServiceImpl;

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
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private Reserva reserva;

    @BeforeEach
    void setUp() {
        reserva = new Reserva();
        reserva.setId(1L);
    }

    @Test
    void testFindAll_ReturnsList() {
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));

        List<Reserva> reservas = reservaService.findAll();

        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertTrue(reservas.contains(reserva));
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        when(reservaRepository.findAll()).thenReturn(List.of());

        List<Reserva> reservas = reservaService.findAll();

        assertNotNull(reservas);
        assertTrue(reservas.isEmpty());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Exists() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Reserva found = reservaService.getById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotExists() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reservaService.getById(1L));
    }

    @Test
    void testSaveReserva() {
        when(reservaRepository.saveAndFlush(reserva)).thenReturn(reserva);

        Reserva saved = reservaService.save(reserva);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(reservaRepository, times(1)).saveAndFlush(reserva);
    }

    @Test
    void testDeleteById() {
        doNothing().when(reservaRepository).deleteById(1L);

        reservaService.deleteById(1L);

        verify(reservaRepository, times(1)).deleteById(1L);
    }
}

