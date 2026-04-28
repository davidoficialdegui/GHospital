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

@ExtendWith(MockitoExtension.class)
class CitaServiceCancelacionTest {

    @Mock private CitaRepository citaRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private MedicoRepository medicoRepository;

    @InjectMocks
    private CitaService citaService;

    private Paciente paciente;
    private Medico medico;
    private Cita cita;

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

    //cancelar una cita pendiente
    @Test
    void cancelarCita_citaPendiente_devuelveEstadoCancelada() {
        
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaDTO resultado = citaService.cancelarCita(1L, 1L);


        assertEquals("CANCELADA", resultado.getEstado());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    //cancelar una cita confirmada 
    @Test
    void cancelarCita_citaConfirmada_devuelveEstadoCancelada() {
        cita.setEstado(Cita.EstadoCita.CONFIRMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaDTO resultado = citaService.cancelarCita(1L, 1L);

        assertEquals("CANCELADA", resultado.getEstado());
    }

    // cancelar una cita que no existe
    @Test
    void cancelarCita_citaNoExiste_lanzaExcepcion() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> citaService.cancelarCita(99L, 1L));

        assertTrue(ex.getMessage().contains("Cita no encontrada"));
        verify(citaRepository, never()).save(any());
    }

    //un paciente intenta cancelar la cita de otro paciente
    @Test
    void cancelarCita_pacienteNoEsDueno_lanzaExcepcion() {
        
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> citaService.cancelarCita(1L, 2L));

        assertTrue(ex.getMessage().contains("No tienes permiso"));
        verify(citaRepository, never()).save(any());
    }

    //intentar cancelar una cita ya cancelada 
    @Test
    void cancelarCita_citaYaCancelada_lanzaExcepcion() {
        cita.setEstado(Cita.EstadoCita.CANCELADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> citaService.cancelarCita(1L, 1L));

        assertTrue(ex.getMessage().contains("ya está cancelada"));
        verify(citaRepository, never()).save(any());
    }

    //intentar cancelar una cita ya realizada
    @Test
    void cancelarCita_citaRealizada_lanzaExcepcion() {
        cita.setEstado(Cita.EstadoCita.REALIZADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> citaService.cancelarCita(1L, 1L));

        assertTrue(ex.getMessage().contains("ya realizada"));
        verify(citaRepository, never()).save(any());
    }
}