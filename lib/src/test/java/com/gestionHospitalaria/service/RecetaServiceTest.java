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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

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

        medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Carlos");
        medico.setApellido1("García");

        receta = new Receta();
        receta.setId(10L);
        receta.setPaciente(paciente);
        receta.setMedico(medico);
        receta.setMedicamento("Ibuprofeno 600mg");
        receta.setDosis("1 comprimido");
        receta.setPosologia("Cada 8 horas con las comidas");
        receta.setDuracionDias(7);
        receta.setFechaEmision(LocalDate.now());
    }

    // ── crearReceta ───────────────────────────────────────────────────────

    @Test
    void crearReceta_correcto_devuelveDTO() {
        // Arrange
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setMedicamento("Ibuprofeno 600mg");
        dto.setDosis("1 comprimido");
        dto.setPosologia("Cada 8 horas con las comidas");
        dto.setDuracionDias(7);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(recetaRepository.save(any(Receta.class))).thenReturn(receta);

        // Act
        RecetaDTO resultado = recetaService.crearReceta(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("Ibuprofeno 600mg", resultado.getMedicamento());
        assertEquals("1 comprimido", resultado.getDosis());
        assertEquals("Ana Martínez", resultado.getPacienteNombre());
        assertEquals("Carlos García", resultado.getMedicoNombre());
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    void crearReceta_medicamentoVacio_lanzaExcepcion() {
        // Arrange
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setMedicamento("   "); // en blanco
        dto.setDosis("1 comprimido");
        dto.setPosologia("Cada 8 horas");

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> recetaService.crearReceta(dto)
        );
        assertTrue(ex.getMessage().contains("medicamento"));
        verify(recetaRepository, never()).save(any());
    }

    @Test
    void crearReceta_medicamentoNull_lanzaExcepcion() {
        // Arrange
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setMedicamento(null);
        dto.setDosis("1 comprimido");
        dto.setPosologia("Cada 8 horas");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> recetaService.crearReceta(dto));
        verify(recetaRepository, never()).save(any());
    }

    @Test
    void crearReceta_dosisVacia_lanzaExcepcion() {
        // Arrange
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setMedicamento("Ibuprofeno 600mg");
        dto.setDosis("   "); // en blanco
        dto.setPosologia("Cada 8 horas");

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> recetaService.crearReceta(dto)
        );
        assertTrue(ex.getMessage().contains("dosis"));
        verify(recetaRepository, never()).save(any());
    }

    @Test
    void crearReceta_posologiaVacia_lanzaExcepcion() {
        // Arrange
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setMedicamento("Ibuprofeno 600mg");
        dto.setDosis("1 comprimido");
        dto.setPosologia("   "); // en blanco

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> recetaService.crearReceta(dto)
        );
        assertTrue(ex.getMessage().contains("posología"));
        verify(recetaRepository, never()).save(any());
    }

    @Test
    void crearReceta_pacienteNoExiste_lanzaExcepcion() {
        // Arrange
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(999L);
        dto.setMedicoId(1L);
        dto.setMedicamento("Ibuprofeno 600mg");
        dto.setDosis("1 comprimido");
        dto.setPosologia("Cada 8 horas");

        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> recetaService.crearReceta(dto));
        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
        verify(recetaRepository, never()).save(any());
    }

    @Test
    void crearReceta_medicoNoExiste_lanzaExcepcion() {
        // Arrange
        CrearRecetaDTO dto = new CrearRecetaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(999L);
        dto.setMedicamento("Ibuprofeno 600mg");
        dto.setDosis("1 comprimido");
        dto.setPosologia("Cada 8 horas");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> recetaService.crearReceta(dto));
        assertTrue(ex.getMessage().contains("Médico no encontrado"));
        verify(recetaRepository, never()).save(any());
    }

    // ── obtenerRecetasPaciente ────────────────────────────────────────────

    @Test
    void obtenerRecetasPaciente_conRecetas_devuelveLista() {
        // Arrange
        Receta r2 = new Receta();
        r2.setId(20L);
        r2.setPaciente(paciente);
        r2.setMedico(medico);
        r2.setMedicamento("Paracetamol 1g");
        r2.setDosis("1 comprimido");
        r2.setPosologia("Cada 6 horas");
        r2.setFechaEmision(LocalDate.now());

        when(pacienteRepository.existsById(1L)).thenReturn(true);
        when(recetaRepository.findByPacienteIdOrderByFechaEmisionDesc(1L))
            .thenReturn(Arrays.asList(receta, r2));

        // Act
        List<RecetaDTO> resultado = recetaService.obtenerRecetasPaciente(1L);

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Ibuprofeno 600mg", resultado.get(0).getMedicamento());
        assertEquals("Paracetamol 1g", resultado.get(1).getMedicamento());
    }

    @Test
    void obtenerRecetasPaciente_sinRecetas_devuelveListaVacia() {
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        when(recetaRepository.findByPacienteIdOrderByFechaEmisionDesc(1L))
            .thenReturn(List.of());

        List<RecetaDTO> resultado = recetaService.obtenerRecetasPaciente(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerRecetasPaciente_pacienteNoExiste_lanzaExcepcion() {
        when(pacienteRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> recetaService.obtenerRecetasPaciente(999L));
        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
    }

    // ── obtenerRecetaPorId ────────────────────────────────────────────────

    @Test
    void obtenerRecetaPorId_existe_devuelveDTO() {
        when(recetaRepository.findById(10L)).thenReturn(Optional.of(receta));

        RecetaDTO resultado = recetaService.obtenerRecetaPorId(10L);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("Ibuprofeno 600mg", resultado.getMedicamento());
    }

    @Test
    void obtenerRecetaPorId_noExiste_lanzaExcepcion() {
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> recetaService.obtenerRecetaPorId(999L));
        assertTrue(ex.getMessage().contains("Receta no encontrada"));
    }
}