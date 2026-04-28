package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.facade.CitaFacade;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración del controlador de citas (vista).
 * Separados de los tests unitarios mediante @Tag("integration").
 */
@Tag("integration")
@WebMvcTest(controllers = CitaViewController.class,
            excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CitaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaFacade citaFacade;

    @Test
    void getCitasGestor_devuelve200() throws Exception {
        CitaDTO cita = new CitaDTO();
        cita.setId(1L);
        cita.setEstado("PENDIENTE");
        cita.setMotivo("Revisión");
        cita.setEspecialidad("Cardiología");
        cita.setMedicoNombre("Carlos García");
        cita.setFechaHora(LocalDateTime.now());

        when(citaFacade.obtenerTodasLasCitas()).thenReturn(List.of(cita));

        mockMvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(view().name("citas"));
    }

    @Test
    void getCrearCita_devuelve200() throws Exception {
        mockMvc.perform(get("/citas/crear"))
                .andExpect(status().isOk())
                .andExpect(view().name("crear-cita"));
    }

    @Test
    void getVerCitas_devuelve200() throws Exception {
        when(citaFacade.obtenerCitasPaciente(anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/citas/ver").param("pacienteId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ver-citas"));
    }

    @Test
    void getCitasGestor_sinCitas_devuelve200() throws Exception {
        when(citaFacade.obtenerTodasLasCitas()).thenReturn(List.of());

        mockMvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(view().name("citas"));
    }

    @Test
    void getTest_devuelveFUNCIONA() throws Exception {
        mockMvc.perform(get("/citas/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("FUNCIONA"));
    }
}
