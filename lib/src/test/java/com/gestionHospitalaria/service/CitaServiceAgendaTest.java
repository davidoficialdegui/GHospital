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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceAgendaTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private CitaService citaService;

    private Medico medico;
    private Paciente paciente;
    private Cita cita;

    @BeforeEach
    void setUp() {
        medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Carlos");
        medico.setApellido1("García");
        medico.setEspecialidad("Cardiología");

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Ana");
        paciente.setApellido1("Martínez");

        cita = new Cita();
        cita.setId(1L);
        cita.setMedico(medico);
        cita.setPaciente(paciente);
        cita.setFechaHora(LocalDateTime.now());
        cita.setMotivo("Revisión");
        cita.setEstado(Cita.EstadoCita.CONFIRMADA);
        cita.setEspecialidad("Cardiología");
    }

    @Test
    void obtenerAgendaDelDia_conCitas_devuelveLista() {
        // Arrange
        when(citaRepository.findByMedicoIdAndFechaHoraBetween(
            eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(cita));

        // Act
        List<CitaDTO> resultado = citaService.obtenerAgendaDelDia(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Revisión", resultado.get(0).getMotivo());
        assertEquals("CONFIRMADA", resultado.get(0).getEstado());
    }

    @Test
    void obtenerAgendaDelDia_sinCitas_devuelveListaVacia() {
        when(citaRepository.findByMedicoIdAndFechaHoraBetween(
            eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of());

        List<CitaDTO> resultado = citaService.obtenerAgendaDelDia(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerAgendaDelDia_variasEspecialidades_mapeoCorrector() {
        Cita c2 = new Cita();
        c2.setId(2L);
        c2.setMedico(medico);
        c2.setPaciente(paciente);
        c2.setFechaHora(LocalDateTime.now().plusHours(2));
        c2.setMotivo("Urgencia");
        c2.setEstado(Cita.EstadoCita.PENDIENTE);
        c2.setEspecialidad("Cardiología");

        when(citaRepository.findByMedicoIdAndFechaHoraBetween(
            eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(Arrays.asList(cita, c2));

        List<CitaDTO> resultado = citaService.obtenerAgendaDelDia(1L);

        assertEquals(2, resultado.size());
        assertEquals("CONFIRMADA", resultado.get(0).getEstado());
        assertEquals("PENDIENTE", resultado.get(1).getEstado());
    }
}