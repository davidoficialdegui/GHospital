package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CrearDispensacionDTO;
import com.gestionHospitalaria.dto.DispensacionDTO;
import com.gestionHospitalaria.entity.Dispensacion;
import com.gestionHospitalaria.entity.Farmaceutico;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.entity.Receta;
import com.gestionHospitalaria.repository.DispensacionRepository;
import com.gestionHospitalaria.repository.FarmaceuticoRepository;
import com.gestionHospitalaria.repository.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class DispensacionServiceTest {

    @Mock private DispensacionRepository dispensacionRepository;
    @Mock private RecetaRepository recetaRepository;
    @Mock private FarmaceuticoRepository farmaceuticoRepository;

    @InjectMocks
    private DispensacionService dispensacionService;

    private Paciente paciente;
    private Medico medico;
    private Receta receta;
    private Farmaceutico farmaceutico;
    private Dispensacion dispensacion;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Ana");
        paciente.setApellido1("Martínez");

        medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Carlos");
        medico.setApellido1("García");

        receta = new Receta();
        receta.setId(10L);
        receta.setPaciente(paciente);
        receta.setMedico(medico);
        receta.setMedicamento("Ibuprofeno");
        receta.setDosis("400mg");
        receta.setPosologia("1 comprimido cada 8 horas");
        receta.setFechaEmision(LocalDate.now());

        farmaceutico = new Farmaceutico();
        farmaceutico.setId(5L);
        farmaceutico.setNombre("Laura");
        farmaceutico.setApellido1("Sánchez");

        dispensacion = new Dispensacion();
        dispensacion.setId(100L);
        dispensacion.setReceta(receta);
        dispensacion.setFarmaceutico(farmaceutico);
        dispensacion.setCantidadDispensada(2);
        dispensacion.setEstado(Dispensacion.EstadoDispensacion.DISPENSADO);
        dispensacion.setObservaciones("Sin incidencias");
        dispensacion.setFechaDispensacion(LocalDateTime.now());
    }

    // ── registrarDispensacion ─────────────────────────────────────────────

    @Test
    void registrarDispensacion_correcto_devuelveDTO() {
        CrearDispensacionDTO dto = new CrearDispensacionDTO();
        dto.setRecetaId(10L);
        dto.setFarmaceuticoId(5L);
        dto.setCantidadDispensada(2);
        dto.setEstado("DISPENSADO");
        dto.setObservaciones("Sin incidencias");

        when(recetaRepository.findById(10L)).thenReturn(Optional.of(receta));
        when(farmaceuticoRepository.findById(5L)).thenReturn(Optional.of(farmaceutico));
        when(dispensacionRepository.save(any(Dispensacion.class))).thenReturn(dispensacion);

        DispensacionDTO resultado = dispensacionService.registrarDispensacion(dto);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals("Ibuprofeno", resultado.getMedicamento());
        assertEquals("Ana Martínez", resultado.getPacienteNombre());
        assertEquals("Laura Sánchez", resultado.getFarmaceuticoNombre());
        assertEquals(2, resultado.getCantidadDispensada());
        assertEquals("DISPENSADO", resultado.getEstado());
        verify(dispensacionRepository, times(1)).save(any(Dispensacion.class));
    }

    @Test
    void registrarDispensacion_cantidadCero_lanzaExcepcion() {
        CrearDispensacionDTO dto = new CrearDispensacionDTO();
        dto.setRecetaId(10L);
        dto.setFarmaceuticoId(5L);
        dto.setCantidadDispensada(0);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> dispensacionService.registrarDispensacion(dto)
        );
        assertTrue(ex.getMessage().contains("cantidad dispensada"));
        verify(dispensacionRepository, never()).save(any());
    }

    @Test
    void registrarDispensacion_cantidadNull_lanzaExcepcion() {
        CrearDispensacionDTO dto = new CrearDispensacionDTO();
        dto.setRecetaId(10L);
        dto.setFarmaceuticoId(5L);
        dto.setCantidadDispensada(null);

        assertThrows(IllegalArgumentException.class,
            () -> dispensacionService.registrarDispensacion(dto));
        verify(dispensacionRepository, never()).save(any());
    }

    @Test
    void registrarDispensacion_recetaNoExiste_lanzaExcepcion() {
        CrearDispensacionDTO dto = new CrearDispensacionDTO();
        dto.setRecetaId(999L);
        dto.setFarmaceuticoId(5L);
        dto.setCantidadDispensada(1);

        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> dispensacionService.registrarDispensacion(dto));
        assertTrue(ex.getMessage().contains("Receta no encontrada"));
        verify(dispensacionRepository, never()).save(any());
    }

    @Test
    void registrarDispensacion_farmaceuticoNoExiste_lanzaExcepcion() {
        CrearDispensacionDTO dto = new CrearDispensacionDTO();
        dto.setRecetaId(10L);
        dto.setFarmaceuticoId(999L);
        dto.setCantidadDispensada(1);

        when(recetaRepository.findById(10L)).thenReturn(Optional.of(receta));
        when(farmaceuticoRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> dispensacionService.registrarDispensacion(dto));
        assertTrue(ex.getMessage().contains("Farmacéutico no encontrado"));
        verify(dispensacionRepository, never()).save(any());
    }

    @Test
    void registrarDispensacion_sinEstado_usaEstadoPorDefecto() {
        CrearDispensacionDTO dto = new CrearDispensacionDTO();
        dto.setRecetaId(10L);
        dto.setFarmaceuticoId(5L);
        dto.setCantidadDispensada(1);
        dto.setEstado(null);

        when(recetaRepository.findById(10L)).thenReturn(Optional.of(receta));
        when(farmaceuticoRepository.findById(5L)).thenReturn(Optional.of(farmaceutico));
        when(dispensacionRepository.save(any(Dispensacion.class))).thenReturn(dispensacion);

        DispensacionDTO resultado = dispensacionService.registrarDispensacion(dto);

        assertNotNull(resultado);
        verify(dispensacionRepository, times(1)).save(any(Dispensacion.class));
    }

    // ── obtenerPorFarmaceutico ────────────────────────────────────────────

    @Test
    void obtenerPorFarmaceutico_conDispensaciones_devuelveLista() {
        when(farmaceuticoRepository.existsById(5L)).thenReturn(true);
        when(dispensacionRepository.findByFarmaceuticoIdOrderByFechaDispensacionDesc(5L))
            .thenReturn(List.of(dispensacion));

        List<DispensacionDTO> resultado = dispensacionService.obtenerPorFarmaceutico(5L);

        assertEquals(1, resultado.size());
        assertEquals("Ibuprofeno", resultado.get(0).getMedicamento());
    }

    @Test
    void obtenerPorFarmaceutico_farmaceuticoNoExiste_lanzaExcepcion() {
        when(farmaceuticoRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> dispensacionService.obtenerPorFarmaceutico(999L));
        assertTrue(ex.getMessage().contains("Farmacéutico no encontrado"));
    }

    // ── obtenerPorPaciente ────────────────────────────────────────────────

    @Test
    void obtenerPorPaciente_devuelveLista() {
        when(dispensacionRepository.findByRecetaPacienteIdOrderByFechaDispensacionDesc(1L))
            .thenReturn(List.of(dispensacion));

        List<DispensacionDTO> resultado = dispensacionService.obtenerPorPaciente(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getPacienteId());
    }

    @Test
    void obtenerPorPaciente_sinDispensaciones_devuelveListaVacia() {
        when(dispensacionRepository.findByRecetaPacienteIdOrderByFechaDispensacionDesc(1L))
            .thenReturn(List.of());

        List<DispensacionDTO> resultado = dispensacionService.obtenerPorPaciente(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ── obtenerPorReceta ──────────────────────────────────────────────────

    @Test
    void obtenerPorReceta_recetaNoExiste_lanzaExcepcion() {
        when(recetaRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> dispensacionService.obtenerPorReceta(999L));
        assertTrue(ex.getMessage().contains("Receta no encontrada"));
    }

    @Test
    void obtenerPorReceta_devuelveLista() {
        when(recetaRepository.existsById(10L)).thenReturn(true);
        when(dispensacionRepository.findByRecetaIdOrderByFechaDispensacionDesc(10L))
            .thenReturn(List.of(dispensacion));

        List<DispensacionDTO> resultado = dispensacionService.obtenerPorReceta(10L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getRecetaId());
    }
}
