package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.service.AdminService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración del controlador de administración.
 * Separados de los tests unitarios mediante @Tag("integration").
 */
@Tag("integration")
@WebMvcTest(controllers = AdminViewController.class,
            excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    void getEstadisticas_devuelve200ConStats() throws Exception {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalPacientes", 2L);
        stats.put("totalMedicos", 2L);
        stats.put("totalRecepcionistas", 1L);
        stats.put("totalCitas", 4L);
        stats.put("citasPendientes", 1L);
        stats.put("citasConfirmadas", 1L);
        stats.put("citasCanceladas", 1L);
        stats.put("citasRealizadas", 1L);
        stats.put("totalDiagnosticos", 3L);

        when(adminService.obtenerEstadisticas()).thenReturn(stats);

        mockMvc.perform(get("/admin/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-estadisticas"))
                .andExpect(model().attributeExists("stats"));
    }

    @Test
    void getUsuarios_devuelve200ConListas() throws Exception {
        when(adminService.listarPacientes()).thenReturn(List.of());
        when(adminService.listarMedicos()).thenReturn(List.of());
        when(adminService.listarRecepcionistas()).thenReturn(List.of());

        mockMvc.perform(get("/admin/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-usuarios"))
                .andExpect(model().attributeExists("pacientes"))
                .andExpect(model().attributeExists("medicos"))
                .andExpect(model().attributeExists("recepcionistas"));
    }
}
