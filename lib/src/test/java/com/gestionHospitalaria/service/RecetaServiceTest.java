package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CrearRecetaDTO;
import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.entity.Receta;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;
import com.gestionHospitalaria.repository.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class RecetaServiceTest {

    @Mock private RecetaRepository recetaRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private MedicoRepository medicoRepository;

    @InjectMocks
    private RecetaService recetaService;

    private Paciente paciente;
    private Medico medico;
    private Receta receta;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Ana");
        paciente.setApellido1("Martínez");
        paciente.setDni("12345678A");

        medico = new Medico();
        medico.setId(2L);
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
    }

    // ── crearReceta ───────────────────────────────────────────────────────

    @Test
    void crearReceta_correcto_devuelveDTO() {
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(2L);
        dto.setMedicamento("Ibuprofeno");
        dto.setDosis("400mg");
        dto.setPosologia("1 comprimido cada 8 horas");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(recetaRepository.save(any(Receta.class))).thenReturn(receta);

        RecetaDTO resultado = recetaService.crearReceta(dto);

        assertNotNull(resultado);
        assertEquals("Ibuprofeno", resultado.getMedicamento());
        assertEquals("Ana Martínez", resultado.getPacienteNombre());
        assertEquals("Carlos García", resultado.getMedicoNombre());
        verify(recetaRepository).save(any(Receta.class));
    }

    @Test
    void crearReceta_medicamentoVacio_lanzaExcepcion() {
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(2L);
        dto.setMedicamento("");
        dto.setDosis("400mg");
        dto.setPosologia("1 comprimido cada 8 horas");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> recetaService.crearReceta(dto));
        assertTrue(ex.getMessage().contains("medicamento"));
        verify(recetaRepository, never()).save(any());
    }

    @Test
    void crearReceta_dosisVacia_lanzaExcepcion() {
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(2L);
        dto.setMedicamento("Ibuprofeno");
        dto.setDosis("");
        dto.setPosologia("1 comprimido cada 8 horas");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> recetaService.crearReceta(dto));
        assertTrue(ex.getMessage().contains("dosis"));
        verify(recetaRepository, never()).save(any());
    }

    @Test
    void crearReceta_posologiaVacia_lanzaExcepcion() {
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(2L);
        dto.setMedicamento("Ibuprofeno");
        dto.setDosis("400mg");
        dto.setPosologia("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> recetaService.crearReceta(dto));
        assertTrue(ex.getMessage().contains("posología"));
        verify(recetaRepository, never()).save(any());
    }

    @Test
    void crearReceta_pacienteNoExiste_lanzaExcepcion() {
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(999L);
        dto.setMedicoId(2L);
        dto.setMedicamento("Ibuprofeno");
        dto.setDosis("400mg");
        dto.setPosologia("1 comprimido cada 8 horas");

        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> recetaService.crearReceta(dto));
        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
        verify(recetaRepository, never()).save(any());
    }

    @Test
    void crearReceta_medicoNoExiste_lanzaExcepcion() {
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(999L);
        dto.setMedicamento("Ibuprofeno");
        dto.setDosis("400mg");
        dto.setPosologia("1 comprimido cada 8 horas");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> recetaService.crearReceta(dto));
        assertTrue(ex.getMessage().contains("Médico no encontrado"));
        verify(recetaRepository, never()).save(any());
    }

    // ── obtenerTodasLasRecetas ────────────────────────────────────────────

    @Test
    void obtenerTodasLasRecetas_devuelveLista() {
        when(recetaRepository.findAll()).thenReturn(List.of(receta));

        List<RecetaDTO> resultado = recetaService.obtenerTodasLasRecetas();

        assertEquals(1, resultado.size());
        assertEquals("Ibuprofeno", resultado.get(0).getMedicamento());
        assertEquals("Ana Martínez", resultado.get(0).getPacienteNombre());
    }

    @Test
    void obtenerTodasLasRecetas_sinRecetas_devuelveListaVacia() {
        when(recetaRepository.findAll()).thenReturn(List.of());

        List<RecetaDTO> resultado = recetaService.obtenerTodasLasRecetas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ── obtenerRecetaPorId ────────────────────────────────────────────────

    @Test
    void obtenerRecetaPorId_existe_devuelveDTO() {
        when(recetaRepository.findById(10L)).thenReturn(Optional.of(receta));

        RecetaDTO resultado = recetaService.obtenerRecetaPorId(10L);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("Ibuprofeno", resultado.getMedicamento());
        assertEquals("400mg", resultado.getDosis());
    }

    @Test
    void obtenerRecetaPorId_noExiste_lanzaExcepcion() {
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> recetaService.obtenerRecetaPorId(999L));
        assertTrue(ex.getMessage().contains("Receta no encontrada"));
    }

    // ── obtenerRecetasPaciente ────────────────────────────────────────────

    @Test
    void obtenerRecetasPaciente_devuelveLista() {
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        when(recetaRepository.findByPacienteIdOrderByFechaEmisionDesc(1L))
            .thenReturn(List.of(receta));

        List<RecetaDTO> resultado = recetaService.obtenerRecetasPaciente(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getPacienteId());
    }

    @Test
    void obtenerRecetasPaciente_pacienteNoExiste_lanzaExcepcion() {
        when(pacienteRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> recetaService.obtenerRecetasPaciente(999L));
        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
    }

    // ── obtenerRecetasPorDniPaciente ──────────────────────────────────────

    @Test
    void obtenerRecetasPorDniPaciente_devuelveLista() {
        when(pacienteRepository.findByDni("12345678A")).thenReturn(Optional.of(paciente));
        when(recetaRepository.findByPacienteIdOrderByFechaEmisionDesc(1L))
            .thenReturn(List.of(receta));

        List<RecetaDTO> resultado = recetaService.obtenerRecetasPorDniPaciente("12345678A");

        assertEquals(1, resultado.size());
        assertEquals("Ibuprofeno", resultado.get(0).getMedicamento());
    }

    @Test
    void obtenerRecetasPorDniPaciente_dniNoExiste_lanzaExcepcion() {
        when(pacienteRepository.findByDni("99999999Z")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> recetaService.obtenerRecetasPorDniPaciente("99999999Z"));
        assertTrue(ex.getMessage().contains("Paciente no encontrado con DNI"));
    }

    @Test
    void obtenerRecetasPorDniPaciente_sinRecetas_devuelveListaVacia() {
        when(pacienteRepository.findByDni("12345678A")).thenReturn(Optional.of(paciente));
        when(recetaRepository.findByPacienteIdOrderByFechaEmisionDesc(1L))
            .thenReturn(List.of());

        List<RecetaDTO> resultado = recetaService.obtenerRecetasPorDniPaciente("12345678A");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
