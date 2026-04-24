package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CrearDiagnosticoDTO;
import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.entity.Diagnostico;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.repository.DiagnosticoRepository;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosticoServiceTest {

    @Mock
    private DiagnosticoRepository diagnosticoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private DiagnosticoService diagnosticoService;

    private Paciente paciente;
    private Medico medico;
    private Diagnostico diagnostico;

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

        diagnostico = new Diagnostico();
        diagnostico.setId(10L);
        diagnostico.setPaciente(paciente);
        diagnostico.setMedico(medico);
        diagnostico.setDescripcion("Hipertensión leve");
        diagnostico.setTratamiento("Dieta y ejercicio");
        diagnostico.setObservaciones("Revisión en 3 meses");
        diagnostico.setFechaDiagnostico(LocalDateTime.now());
    }

    // ── registrarDiagnostico ──────────────────────────────────────────────

    @Test
    void registrarDiagnostico_correcto_devuelveDTO() {
        // Arrange
        CrearDiagnosticoDTO dto = new CrearDiagnosticoDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setDescripcion("Hipertensión leve");
        dto.setTratamiento("Dieta y ejercicio");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnostico);

        // Act
        DiagnosticoDTO resultado = diagnosticoService.registrarDiagnostico(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("Hipertensión leve", resultado.getDescripcion());
        assertEquals("Ana Martínez", resultado.getPacienteNombre());
        assertEquals("Carlos García", resultado.getMedicoNombre());
        verify(diagnosticoRepository, times(1)).save(any(Diagnostico.class));
    }

    @Test
    void registrarDiagnostico_descripcionVacia_lanzaExcepcion() {
        // Arrange
        CrearDiagnosticoDTO dto = new CrearDiagnosticoDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setDescripcion("   "); // en blanco

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> diagnosticoService.registrarDiagnostico(dto)
        );
        assertTrue(ex.getMessage().contains("descripción"));
        verify(diagnosticoRepository, never()).save(any());
    }

    @Test
    void registrarDiagnostico_descripcionNull_lanzaExcepcion() {
        // Arrange
        CrearDiagnosticoDTO dto = new CrearDiagnosticoDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setDescripcion(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> diagnosticoService.registrarDiagnostico(dto));
        verify(diagnosticoRepository, never()).save(any());
    }

    @Test
    void registrarDiagnostico_pacienteNoExiste_lanzaExcepcion() {
        // Arrange
        CrearDiagnosticoDTO dto = new CrearDiagnosticoDTO();
        dto.setPacienteId(999L);
        dto.setMedicoId(1L);
        dto.setDescripcion("Diagnóstico válido");

        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> diagnosticoService.registrarDiagnostico(dto));
        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
        verify(diagnosticoRepository, never()).save(any());
    }

    @Test
    void registrarDiagnostico_medicoNoExiste_lanzaExcepcion() {
        // Arrange
        CrearDiagnosticoDTO dto = new CrearDiagnosticoDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(999L);
        dto.setDescripcion("Diagnóstico válido");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> diagnosticoService.registrarDiagnostico(dto));
        assertTrue(ex.getMessage().contains("Médico no encontrado"));
        verify(diagnosticoRepository, never()).save(any());
    }

    @Test
    void registrarDiagnostico_sinTratamiento_seGuardaIgual() {
        // Los campos opcionales (tratamiento, observaciones) pueden ser null
        CrearDiagnosticoDTO dto = new CrearDiagnosticoDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setDescripcion("Solo descripción");
        dto.setTratamiento(null);
        dto.setObservaciones(null);

        Diagnostico sinTratamiento = new Diagnostico();
        sinTratamiento.setId(11L);
        sinTratamiento.setPaciente(paciente);
        sinTratamiento.setMedico(medico);
        sinTratamiento.setDescripcion("Solo descripción");
        sinTratamiento.setFechaDiagnostico(LocalDateTime.now());

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(diagnosticoRepository.save(any())).thenReturn(sinTratamiento);

        DiagnosticoDTO resultado = diagnosticoService.registrarDiagnostico(dto);

        assertNotNull(resultado);
        assertNull(resultado.getTratamiento());
    }

    // ── obtenerDiagnosticosPaciente ───────────────────────────────────────

    @Test
    void obtenerDiagnosticosPaciente_conDiagnosticos_devuelveLista() {
        // Arrange
        Diagnostico d2 = new Diagnostico();
        d2.setId(20L);
        d2.setPaciente(paciente);
        d2.setMedico(medico);
        d2.setDescripcion("Diabetes tipo 2");
        d2.setFechaDiagnostico(LocalDateTime.now());

        when(pacienteRepository.existsById(1L)).thenReturn(true);
        when(diagnosticoRepository.findByPacienteIdOrderByFechaDiagnosticoDesc(1L))
            .thenReturn(Arrays.asList(diagnostico, d2));

        // Act
        List<DiagnosticoDTO> resultado = diagnosticoService.obtenerDiagnosticosPaciente(1L);

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Hipertensión leve", resultado.get(0).getDescripcion());
        assertEquals("Diabetes tipo 2", resultado.get(1).getDescripcion());
    }

    @Test
    void obtenerDiagnosticosPaciente_sinDiagnosticos_devuelveListaVacia() {
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        when(diagnosticoRepository.findByPacienteIdOrderByFechaDiagnosticoDesc(1L))
            .thenReturn(List.of());

        List<DiagnosticoDTO> resultado = diagnosticoService.obtenerDiagnosticosPaciente(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerDiagnosticosPaciente_pacienteNoExiste_lanzaExcepcion() {
        when(pacienteRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> diagnosticoService.obtenerDiagnosticosPaciente(999L));
        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
    }
}