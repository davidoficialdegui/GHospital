package com.gestionHospitalaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionHospitalaria.dto.CrearRecetaDTO;
import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.facade.RecetaFacade;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
@WebMvcTest(controllers = RecetaController.class,
            excludeAutoConfiguration = SecurityAutoConfiguration.class)
class RecetaControllerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(RecetaControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetaFacade recetaFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private RecetaDTO buildRecetaDTO() {
        RecetaDTO dto = new RecetaDTO();
        dto.setId(10L);
        dto.setPacienteId(1L);
        dto.setPacienteNombre("Ana Martínez");
        dto.setMedicoId(2L);
        dto.setMedicoNombre("Carlos García");
        dto.setMedicamento("Ibuprofeno");
        dto.setDosis("400mg");
        dto.setPosologia("1 comprimido cada 8 horas");
        dto.setFechaEmision(LocalDate.now());
        dto.setFechaCreacion(LocalDateTime.now());
        return dto;
    }

    @Test
    void getTodas_devuelveLista() throws Exception {
        log.info("Test de integración: GET /api/recetas devuelve lista de recetas");
        when(recetaFacade.obtenerTodasLasRecetas()).thenReturn(List.of(buildRecetaDTO()));

        mockMvc.perform(get("/api/recetas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].medicamento").value("Ibuprofeno"))
            .andExpect(jsonPath("$[0].pacienteNombre").value("Ana Martínez"));
    }

    @Test
    void getTodas_sinRecetas_devuelveListaVacia() throws Exception {
        when(recetaFacade.obtenerTodasLasRecetas()).thenReturn(List.of());

        mockMvc.perform(get("/api/recetas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getPorId_existe_devuelveDTO() throws Exception {
        when(recetaFacade.obtenerRecetaPorId(eq(10L))).thenReturn(buildRecetaDTO());

        mockMvc.perform(get("/api/recetas/10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.medicamento").value("Ibuprofeno"))
            .andExpect(jsonPath("$.dosis").value("400mg"));
    }

    @Test
    void getPorPaciente_devuelveLista() throws Exception {
        when(recetaFacade.obtenerRecetasPaciente(eq(1L))).thenReturn(List.of(buildRecetaDTO()));

        mockMvc.perform(get("/api/recetas/paciente/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].pacienteNombre").value("Ana Martínez"));
    }

    @Test
    void getPorDniPaciente_devuelveLista() throws Exception {
        when(recetaFacade.obtenerRecetasPorDniPaciente(eq("12345678A")))
            .thenReturn(List.of(buildRecetaDTO()));

        mockMvc.perform(get("/api/recetas/paciente/dni/12345678A"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].medicamento").value("Ibuprofeno"));
    }

    @Test
    void getPorDniPaciente_sinRecetas_devuelveListaVacia() throws Exception {
        when(recetaFacade.obtenerRecetasPorDniPaciente(eq("99999999Z"))).thenReturn(List.of());

        mockMvc.perform(get("/api/recetas/paciente/dni/99999999Z"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void postReceta_correcto_devuelve200() throws Exception {
        CrearRecetaDTO request = new CrearRecetaDTO();
        request.setPacienteId(1L);
        request.setMedicoId(2L);
        request.setMedicamento("Ibuprofeno");
        request.setDosis("400mg");
        request.setPosologia("1 comprimido cada 8 horas");

        when(recetaFacade.crearReceta(any(CrearRecetaDTO.class))).thenReturn(buildRecetaDTO());

        mockMvc.perform(post("/api/recetas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.medicamento").value("Ibuprofeno"));
    }
}
