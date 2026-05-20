package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.facade.ConstanteVitalFacade;
import com.gestionHospitalaria.facade.RecetaFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnfermeroViewControllerTest {

    private static final Logger log = LoggerFactory.getLogger(EnfermeroViewControllerTest.class);

    @Mock
    private ConstanteVitalFacade constanteVitalFacade;

    @Mock
    private RecetaFacade recetaFacade;

    @Mock
    private Model model;

    @InjectMocks
    private EnfermeroViewController enfermeroViewController;

    private RecetaDTO recetaDTO;

    @BeforeEach
    void setUp() {
        log.info("Preparando datos de prueba para EnfermeroViewControllerTest");
        recetaDTO = new RecetaDTO();
        recetaDTO.setId(1L);
        recetaDTO.setPacienteId(1L);
        recetaDTO.setPacienteNombre("Ana Martínez");
        recetaDTO.setMedicoNombre("Carlos García");
        recetaDTO.setMedicamento("Ibuprofeno 600mg");
        recetaDTO.setDosis("1 comprimido");
        recetaDTO.setPosologia("Cada 8 horas con las comidas");
        recetaDTO.setDuracionDias(7);
        recetaDTO.setFechaEmision(LocalDate.now());
    }

    // ── verTratamientos ───────────────────────────────────────────────────

    @Test
    void verTratamientos_pacienteConRecetas_devuelveVistaCorrecta() {
        log.info("Test: enfermero ve tratamientos de paciente con recetas");
        // Arrange
        when(recetaFacade.obtenerRecetasPaciente(1L)).thenReturn(List.of(recetaDTO));

        // Act
        String vista = enfermeroViewController.verTratamientos(1L, model);

        // Assert
        assertEquals("ver-tratamientos-enfermero", vista);
        verify(model).addAttribute("recetas", List.of(recetaDTO));
        verify(model).addAttribute("pacienteId", 1L);
    }

    @Test
    void verTratamientos_pacienteSinRecetas_devuelveListaVacia() {
        // Arrange
        when(recetaFacade.obtenerRecetasPaciente(1L)).thenReturn(Collections.emptyList());

        // Act
        String vista = enfermeroViewController.verTratamientos(1L, model);

        // Assert
        assertEquals("ver-tratamientos-enfermero", vista);
        verify(model).addAttribute("recetas", Collections.emptyList());
        verify(model).addAttribute("pacienteId", 1L);
    }

    @Test
    void verTratamientos_pacienteNoExiste_muestraError() {
        // Arrange
        when(recetaFacade.obtenerRecetasPaciente(999L))
            .thenThrow(new RuntimeException("Paciente no encontrado con id: 999"));

        // Act
        String vista = enfermeroViewController.verTratamientos(999L, model);

        // Assert
        assertEquals("ver-tratamientos-enfermero", vista);
        verify(model).addAttribute(eq("error"), contains("Paciente no encontrado"));
        verify(model).addAttribute(eq("recetas"), eq(Collections.emptyList()));
        verify(model).addAttribute("pacienteId", 999L);
    }

    @Test
    void verTratamientos_variasRecetas_todasSeAnyadenAlModelo() {
        // Arrange
        RecetaDTO receta2 = new RecetaDTO();
        receta2.setId(2L);
        receta2.setMedicamento("Paracetamol 1g");
        receta2.setDosis("1 comprimido");
        receta2.setPosologia("Cada 6 horas");
        receta2.setFechaEmision(LocalDate.now());

        List<RecetaDTO> recetas = List.of(recetaDTO, receta2);
        when(recetaFacade.obtenerRecetasPaciente(1L)).thenReturn(recetas);

        // Act
        String vista = enfermeroViewController.verTratamientos(1L, model);

        // Assert
        assertEquals("ver-tratamientos-enfermero", vista);
        verify(model).addAttribute("recetas", recetas);
        verify(recetaFacade, times(1)).obtenerRecetasPaciente(1L);
    }
}