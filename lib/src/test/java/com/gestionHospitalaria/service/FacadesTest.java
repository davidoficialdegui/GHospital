package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.*;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.facade.CitaFacade;
import com.gestionHospitalaria.facade.DiagnosticoFacade;
import com.gestionHospitalaria.facade.PacienteFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios de las fachadas (Facade Pattern).
 * Verifican que cada fachada delega correctamente en su servicio correspondiente.
 */
@ExtendWith(MockitoExtension.class)
class FacadesTest {

    // ── PacienteFacade ───────────────────────────────────────────

    @Mock
    private PacienteService pacienteService;

    @InjectMocks
    private PacienteFacade pacienteFacade;

    @Mock
    private CitaService citaService;

    @InjectMocks
    private CitaFacade citaFacade;

    @Mock
    private DiagnosticoService diagnosticoService;

    @InjectMocks
    private DiagnosticoFacade diagnosticoFacade;

    // ── PacienteFacade tests ─────────────────────────────────────

    @Test
    void pacienteFacade_registrar_delegaEnServicio() {
        RegistroPacienteDTO dto = new RegistroPacienteDTO();
        dto.setNombre("Ana");
        dto.setEmail("ana@test.com");

        Paciente esperado = new Paciente();
        esperado.setId(1L);
        esperado.setNombre("Ana");

        when(pacienteService.registrar(any(RegistroPacienteDTO.class))).thenReturn(esperado);

        Paciente resultado = pacienteFacade.registrar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pacienteService, times(1)).registrar(dto);
    }

    @Test
    void pacienteFacade_login_delegaEnServicio() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("admin@hospital.com");
        dto.setPassword("admin123");

        when(pacienteService.login(any(LoginDTO.class))).thenReturn("ADMIN|1|Admin");

        String resultado = pacienteFacade.login(dto);

        assertEquals("ADMIN|1|Admin", resultado);
        verify(pacienteService, times(1)).login(dto);
    }

    @Test
    void pacienteFacade_obtenerHistorial_delegaEnServicio() {
        HistorialMedicoDTO historial = new HistorialMedicoDTO();
        historial.setNombreCompleto("Ana Martínez");

        when(pacienteService.obtenerHistorial(1L)).thenReturn(historial);

        HistorialMedicoDTO resultado = pacienteFacade.obtenerHistorial(1L);

        assertNotNull(resultado);
        assertEquals("Ana Martínez", resultado.getNombreCompleto());
        verify(pacienteService, times(1)).obtenerHistorial(1L);
    }

    // ── CitaFacade tests ─────────────────────────────────────────

    @Test
    void citaFacade_crearCita_delegaEnServicio() {
        CrearCitaDTO dto = new CrearCitaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(2L);
        dto.setMotivo("Control");

        CitaDTO esperada = new CitaDTO();
        esperada.setId(10L);
        esperada.setEstado("PENDIENTE");

        when(citaService.crearCita(any(CrearCitaDTO.class))).thenReturn(esperada);

        CitaDTO resultado = citaFacade.crearCita(dto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        verify(citaService, times(1)).crearCita(dto);
    }

    @Test
    void citaFacade_obtenerTodasLasCitas_delegaEnServicio() {
        CitaDTO c1 = new CitaDTO(); c1.setId(1L);
        CitaDTO c2 = new CitaDTO(); c2.setId(2L);

        when(citaService.obtenerTodasLasCitas()).thenReturn(List.of(c1, c2));

        List<CitaDTO> resultado = citaFacade.obtenerTodasLasCitas();

        assertEquals(2, resultado.size());
        verify(citaService, times(1)).obtenerTodasLasCitas();
    }

    @Test
    void citaFacade_obtenerCitasPaciente_delegaEnServicio() {
        CitaDTO cita = new CitaDTO();
        cita.setId(1L);
        cita.setEstado("PENDIENTE");

        when(citaService.obtenerCitasPaciente(1L)).thenReturn(List.of(cita));

        List<CitaDTO> resultado = citaFacade.obtenerCitasPaciente(1L);

        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
        verify(citaService, times(1)).obtenerCitasPaciente(1L);
    }

    @Test
    void citaFacade_obtenerAgendaDelDia_delegaEnServicio() {
        when(citaService.obtenerAgendaDelDia(2L)).thenReturn(List.of());

        List<CitaDTO> resultado = citaFacade.obtenerAgendaDelDia(2L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(citaService, times(1)).obtenerAgendaDelDia(2L);
    }

    @Test
    void citaFacade_cambiarEstado_delegaEnServicio() {
        CitaDTO actualizada = new CitaDTO();
        actualizada.setId(5L);
        actualizada.setEstado("CONFIRMADA");

        when(citaService.cambiarEstado(5L, "CONFIRMADA")).thenReturn(actualizada);

        CitaDTO resultado = citaFacade.cambiarEstado(5L, "CONFIRMADA");

        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(citaService, times(1)).cambiarEstado(5L, "CONFIRMADA");
    }

    @Test
    void citaFacade_cancelarCita_delegaEnServicio() {
        CitaDTO cancelada = new CitaDTO();
        cancelada.setId(3L);
        cancelada.setEstado("CANCELADA");

        when(citaService.cancelarCita(3L, 1L)).thenReturn(cancelada);

        CitaDTO resultado = citaFacade.cancelarCita(3L, 1L);

        assertEquals("CANCELADA", resultado.getEstado());
        verify(citaService, times(1)).cancelarCita(3L, 1L);
    }

    // ── DiagnosticoFacade tests ──────────────────────────────────

    @Test
    void diagnosticoFacade_registrarDiagnostico_delegaEnServicio() {
        CrearDiagnosticoDTO dto = new CrearDiagnosticoDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(2L);
        dto.setDescripcion("Hipertensión");

        DiagnosticoDTO esperado = new DiagnosticoDTO();
        esperado.setId(20L);
        esperado.setDescripcion("Hipertensión");

        when(diagnosticoService.registrarDiagnostico(any(CrearDiagnosticoDTO.class))).thenReturn(esperado);

        DiagnosticoDTO resultado = diagnosticoFacade.registrarDiagnostico(dto);

        assertNotNull(resultado);
        assertEquals(20L, resultado.getId());
        assertEquals("Hipertensión", resultado.getDescripcion());
        verify(diagnosticoService, times(1)).registrarDiagnostico(dto);
    }

    @Test
    void diagnosticoFacade_obtenerDiagnosticosPaciente_delegaEnServicio() {
        DiagnosticoDTO d = new DiagnosticoDTO();
        d.setId(1L);
        d.setDescripcion("Gripe");

        when(diagnosticoService.obtenerDiagnosticosPaciente(1L)).thenReturn(List.of(d));

        List<DiagnosticoDTO> resultado = diagnosticoFacade.obtenerDiagnosticosPaciente(1L);

        assertEquals(1, resultado.size());
        assertEquals("Gripe", resultado.get(0).getDescripcion());
        verify(diagnosticoService, times(1)).obtenerDiagnosticosPaciente(1L);
    }

    @Test
    void diagnosticoFacade_obtenerDiagnosticosPaciente_sinDiagnosticos_listaVacia() {
        when(diagnosticoService.obtenerDiagnosticosPaciente(99L)).thenReturn(List.of());

        List<DiagnosticoDTO> resultado = diagnosticoFacade.obtenerDiagnosticosPaciente(99L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(diagnosticoService, times(1)).obtenerDiagnosticosPaciente(99L);
    }
}
