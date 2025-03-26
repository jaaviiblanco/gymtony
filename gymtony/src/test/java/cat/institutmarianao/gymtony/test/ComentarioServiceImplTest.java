package cat.institutmarianao.gymtony.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import cat.institutmarianao.gymtony.model.Cliente;
import cat.institutmarianao.gymtony.model.Comentario;
import cat.institutmarianao.gymtony.repositories.ComentarioRepository;
import cat.institutmarianao.gymtony.services.impl.ComentarioServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ComentarioServiceImplTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private ComentarioServiceImpl comentarioService;

    private Cliente cliente;
    private Comentario comentario1;
    private Comentario comentario2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setUsername("cliente1");
        cliente.setEmail("cliente@test.com");

        comentario1 = new Comentario(cliente, "Buen gimnasio", 5);
        comentario1.setId(1L);

        comentario2 = new Comentario(cliente, "Mal servicio", 2);
        comentario2.setId(2L);
    }

    @Test
    void testFindAll() {
        when(comentarioRepository.findAll()).thenReturn(Arrays.asList(comentario1, comentario2));

        List<Comentario> comentarios = comentarioService.findAll();

        assertNotNull(comentarios);
        assertEquals(2, comentarios.size());
        verify(comentarioRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdExists() {
        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(comentario1));

        Optional<Comentario> result = comentarioService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Buen gimnasio", result.get().getComentario());
        verify(comentarioRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotExists() {
        when(comentarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Comentario> result = comentarioService.findById(99L);

        assertFalse(result.isPresent());
        verify(comentarioRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveComentario() {
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentario1);

        Comentario savedComentario = comentarioService.save(comentario1);

        assertNotNull(savedComentario);
        assertEquals("Buen gimnasio", savedComentario.getComentario());
        verify(comentarioRepository, times(1)).save(comentario1);
    }

    @Test
    void testDeleteById() {
        doNothing().when(comentarioRepository).deleteById(1L);

        comentarioService.deleteById(1L);

        verify(comentarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByClienteId() {
        when(comentarioRepository.findByClienteId(1L)).thenReturn(Arrays.asList(comentario1, comentario2));

        List<Comentario> result = comentarioService.findByClienteId(1L);

        assertEquals(2, result.size());
        verify(comentarioRepository, times(1)).findByClienteId(1L);
    }

    @Test
    void testFindByCalificacion() {
        when(comentarioRepository.findByCalificacion(5)).thenReturn(List.of(comentario1));

        List<Comentario> result = comentarioService.findByCalificacion(5);

        assertEquals(1, result.size());
        assertEquals("Buen gimnasio", result.get(0).getComentario());
        verify(comentarioRepository, times(1)).findByCalificacion(5);
    }
}
