package com.gestionHospitalaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionHospitalaria.dto.CrearDispensacionDTO;
import com.gestionHospitalaria.dto.DispensacionDTO;
import com.gestionHospitalaria.facade.DispensacionFacade;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration")
@WebMvcTest(controllers = DispensacionController.class,
            excludeAutoConfiguration = SecurityAutoConfiguration.class)
class DispensacionControllerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(DispensacionControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DispensacionFacade dispensacionFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postDispensacion_correcto_devuelve200ConDTO() throws Exception {
        log.info("Test de integración: POST /api/dispensaciones devuelve 200 con DTO");
        CrearDispensacionDTO request = new CrearDispensacionDTO();
        request.setRecetaId(10L);
        request.setFarmaceuticoId(5L);
        request.setCantidadDispensada(2);
        request.setEstado("DISPENSADO");

        DispensacionDTO response = new DispensacionDTO();
        response.setId(100L);
        response.setRecetaId(10L);
        response.setMedicamento("Ibuprofeno");
        response.setPacienteId(1L);
        response.setPacienteNombre("Ana Martínez");
        response.setFarmaceuticoId(5L);
        response.setFarmaceuticoNombre("Laura Sánchez");
        response.setCantidadDispensada(2);
        response.setEstado("DISPENSADO");
        response.setFechaDispensacion(LocalDateTime.now());

        when(dispensacionFacade.registrarDispensacion(any(CrearDispensacionDTO.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/dispensaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.medicamento").value("Ibuprofeno"))
            .andExpect(jsonPath("$.pacienteNombre").value("Ana Martínez"))
            .andExpect(jsonPath("$.farmaceuticoNombre").value("Laura Sánchez"))
            .andExpect(jsonPath("$.estado").value("DISPENSADO"));
    }

    @Test
    void getDispensacionesPorPaciente_devuelveLista() throws Exception {
        DispensacionDTO d = new DispensacionDTO();
        d.setId(100L);
        d.setPacienteId(1L);
        d.setMedicamento("Paracetamol");
        d.setEstado("DISPENSADO");
        d.setFechaDispensacion(LocalDateTime.now());

        when(dispensacionFacade.obtenerPorPaciente(eq(1L))).thenReturn(List.of(d));

        mockMvc.perform(get("/api/dispensaciones/paciente/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].medicamento").value("Paracetamol"));
    }

    @Test
    void getDispensacionesPorPaciente_sinDispensaciones_devuelveListaVacia() throws Exception {
        when(dispensacionFacade.obtenerPorPaciente(eq(99L))).thenReturn(List.of());

        mockMvc.perform(get("/api/dispensaciones/paciente/99"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getDispensacionesPorFarmaceutico_devuelveLista() throws Exception {
        DispensacionDTO d = new DispensacionDTO();
        d.setId(100L);
        d.setFarmaceuticoId(5L);
        d.setMedicamento("Ibuprofeno");
        d.setEstado("DISPENSADO");
        d.setFechaDispensacion(LocalDateTime.now());

        when(dispensacionFacade.obtenerPorFarmaceutico(eq(5L))).thenReturn(List.of(d));

        mockMvc.perform(get("/api/dispensaciones/farmaceutico/5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].medicamento").value("Ibuprofeno"));
    }

    @Test
    void getDispensacionesPorReceta_devuelveLista() throws Exception {
        DispensacionDTO d = new DispensacionDTO();
        d.setId(100L);
        d.setRecetaId(10L);
        d.setMedicamento("Amoxicilina");
        d.setEstado("PARCIAL");
        d.setFechaDispensacion(LocalDateTime.now());

        when(dispensacionFacade.obtenerPorReceta(eq(10L))).thenReturn(List.of(d));

        mockMvc.perform(get("/api/dispensaciones/receta/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].estado").value("PARCIAL"));
    }
}
