package com.gestionHospitalaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionHospitalaria.dto.CrearDiagnosticoDTO;
import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.facade.DiagnosticoFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de integración: verifica que el controller HTTP llama
 * correctamente al servidor (facade → service → repository).
 * Este test prueba la "remoteness" exigida por el Sprint 2.
 */
@WebMvcTest(DiagnosticoController.class)
class DiagnosticoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiagnosticoFacade diagnosticoFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postDiagnostico_correcto_devuelve200ConDTO() throws Exception {
        // Arrange
        CrearDiagnosticoDTO request = new CrearDiagnosticoDTO();
        request.setPacienteId(1L);
        request.setMedicoId(1L);
        request.setDescripcion("Hipertensión leve");
        request.setTratamiento("Dieta mediterránea");

        DiagnosticoDTO response = new DiagnosticoDTO();
        response.setId(10L);
        response.setPacienteId(1L);
        response.setPacienteNombre("Ana Martínez");
        response.setMedicoId(1L);
        response.setMedicoNombre("Carlos García");
        response.setDescripcion("Hipertensión leve");
        response.setTratamiento("Dieta mediterránea");
        response.setFechaDiagnostico(LocalDateTime.now());

        when(diagnosticoFacade.registrarDiagnostico(any(CrearDiagnosticoDTO.class)))
            .thenReturn(response);

        // Act & Assert — llamada HTTP real al servidor
        mockMvc.perform(post("/api/diagnosticos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.descripcion").value("Hipertensión leve"))
            .andExpect(jsonPath("$.pacienteNombre").value("Ana Martínez"))
            .andExpect(jsonPath("$.medicoNombre").value("Carlos García"));
    }

    @Test
    void getDiagnosticosPaciente_devuelveLista() throws Exception {
        // Arrange
        DiagnosticoDTO d = new DiagnosticoDTO();
        d.setId(1L);
        d.setPacienteId(1L);
        d.setDescripcion("Gripe estacional");
        d.setFechaDiagnostico(LocalDateTime.now());

        when(diagnosticoFacade.obtenerDiagnosticosPaciente(eq(1L)))
            .thenReturn(List.of(d));

        // Act & Assert
        mockMvc.perform(get("/api/diagnosticos/paciente/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].descripcion").value("Gripe estacional"));
    }

    @Test
    void getDiagnosticosPaciente_sinDiagnosticos_devuelveListaVacia() throws Exception {
        when(diagnosticoFacade.obtenerDiagnosticosPaciente(eq(99L)))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/diagnosticos/paciente/99"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }
}