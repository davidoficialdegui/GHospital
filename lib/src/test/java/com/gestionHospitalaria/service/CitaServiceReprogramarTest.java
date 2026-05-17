package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.entity.Cita;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.repository.CitaRepository;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para reprogramar citas y obtenerCitaPorId.
 * Historia de usuario: Como recepcionista, quiero poder reprogramar citas.
 */
@ExtendWith(MockitoExtension.class)
class CitaServiceReprogramarTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private CitaService citaService;

    private Paciente paciente;
    private Medico medico;
    private Cita cita;
    private final String nuevaFecha = "2026-06-15T10:30";

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
        medico.setEspecialidad("Cardiología");

        cita = new Cita();
        cita.setId(1L);
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaHora(LocalDateTime.now().plusDays(1));
        cita.setEstado(Cita.EstadoCita.PENDIENTE);
        cita.setMotivo("Revisión");
        cita.setEspecialidad("Cardiología");
    }

    // ── reprogramarCita ──────────────────────────────────────────

    @Test
    void reprogramarCita_pendiente_actualizaFechaYEstado() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaDTO resultado = citaService.reprogramarCita(1L, nuevaFecha);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(citaRepository).findById(1L);
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    void reprogramarCita_confirmada_reprogramaCorrectamente() {
        cita.setEstado(Cita.EstadoCita.CONFIRMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaDTO resultado = citaService.reprogramarCita(1L, nuevaFecha);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    void reprogramarCita_cancelada_lanzaExcepcion() {
        cita.setEstado(Cita.EstadoCita.CANCELADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> citaService.reprogramarCita(1L, nuevaFecha));

        assertTrue(ex.getMessage().contains("cancelada"));
        verify(citaRepository, never()).save(any());
    }

    @Test
    void reprogramarCita_realizada_lanzaExcepcion() {
        cita.setEstado(Cita.EstadoCita.REALIZADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> citaService.reprogramarCita(1L, nuevaFecha));

        assertTrue(ex.getMessage().contains("realizada"));
        verify(citaRepository, never()).save(any());
    }

    @Test
    void reprogramarCita_citaNoExiste_lanzaExcepcion() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> citaService.reprogramarCita(99L, nuevaFecha));

        assertTrue(ex.getMessage().contains("Cita no encontrada"));
        verify(citaRepository, never()).save(any());
    }

    @Test
    void reprogramarCita_nuevaFechaSeGuardaCorrectamente() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> {
            Cita c = inv.getArgument(0);
            assertEquals(LocalDateTime.parse(nuevaFecha), c.getFechaHora());
            return c;
        });

        citaService.reprogramarCita(1L, nuevaFecha);

        verify(citaRepository).save(any(Cita.class));
    }

    // ── obtenerCitaPorId ─────────────────────────────────────────

    @Test
    void obtenerCitaPorId_existente_devuelveDTO() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        CitaDTO resultado = citaService.obtenerCitaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals("Revisión", resultado.getMotivo());
        verify(citaRepository).findById(1L);
    }

    @Test
    void obtenerCitaPorId_noExiste_lanzaExcepcion() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> citaService.obtenerCitaPorId(99L));

        assertTrue(ex.getMessage().contains("Cita no encontrada"));
        verify(citaRepository).findById(99L);
    }
}
