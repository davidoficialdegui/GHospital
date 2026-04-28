package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InformeMedicoServiceTest {

    private InformeMedicoService informeMedicoService;

    @BeforeEach
    void setUp() {
        informeMedicoService = new InformeMedicoService();
    }

    @Test
    void generarInformePdf_conDatos_devuelvePdfNoVacio() {
        HistorialMedicoDTO historial = new HistorialMedicoDTO();
        historial.setNombreCompleto("Ana Martínez");
        historial.setGrupoSanguineo("A+");
        historial.setAltura(1.65);
        historial.setPeso(60.0);
        historial.setAlergias("Polen");
        historial.setMedicacionActual("Ibuprofeno");

        DiagnosticoDTO diagnostico = new DiagnosticoDTO();
        diagnostico.setDescripcion("Hipertensión leve");
        diagnostico.setTratamiento("Dieta y ejercicio");
        diagnostico.setObservaciones("Revisión en 3 meses");
        diagnostico.setMedicoNombre("Carlos García");
        diagnostico.setFechaDiagnostico(LocalDateTime.now());

        byte[] pdf = informeMedicoService.generarInformePdf(
                historial,
                List.of(diagnostico)
        );

        assertNotNull(pdf);
        assertTrue(pdf.length > 500);
    }

    @Test
    void generarInformePdf_sinDiagnosticos_devuelvePdfNoVacio() {
        HistorialMedicoDTO historial = new HistorialMedicoDTO();
        historial.setNombreCompleto("Pedro López");
        historial.setGrupoSanguineo("B-");

        byte[] pdf = informeMedicoService.generarInformePdf(
                historial,
                List.of()
        );

        assertNotNull(pdf);
        assertTrue(pdf.length > 300);
    }

    @Test
    void generarInformePdf_conDatos_empiezaConCabeceraPdf() {
        HistorialMedicoDTO historial = new HistorialMedicoDTO();
        historial.setNombreCompleto("María García");

        byte[] pdf = informeMedicoService.generarInformePdf(
                historial,
                List.of()
        );

        String inicio = new String(pdf, 0, 4);

        assertEquals("%PDF", inicio);
    }

    @Test
    void generarInformePdf_camposNulos_noLanzaExcepcion() {
        HistorialMedicoDTO historial = new HistorialMedicoDTO();
        historial.setNombreCompleto("Paciente Sin Datos");

        assertDoesNotThrow(() ->
                informeMedicoService.generarInformePdf(
                        historial,
                        List.of()
                )
        );
    }

    @Test
    void generarInformePdf_variosDiagnosticos_devuelvePdf() {
        HistorialMedicoDTO historial = new HistorialMedicoDTO();
        historial.setNombreCompleto("Luis Pérez");

        DiagnosticoDTO d1 = new DiagnosticoDTO();
        d1.setDescripcion("Diabetes tipo 2");
        d1.setMedicoNombre("Dr. García");
        d1.setFechaDiagnostico(LocalDateTime.now());

        DiagnosticoDTO d2 = new DiagnosticoDTO();
        d2.setDescripcion("Hipertensión");
        d2.setTratamiento("Enalapril 10mg");
        d2.setMedicoNombre("Dr. López");
        d2.setFechaDiagnostico(LocalDateTime.now().minusDays(10));

        byte[] pdf = informeMedicoService.generarInformePdf(
                historial,
                List.of(d1, d2)
        );

        assertNotNull(pdf);
        assertTrue(pdf.length > 500);
    }

    @Test
    void generarInformePdf_historialNull_lanzaExcepcion() {
        assertThrows(Exception.class, () ->
                informeMedicoService.generarInformePdf(
                        null,
                        List.of()
                )
        );
    }

    @Test
    void generarInformePdf_listaDiagnosticosNull_noFalla() {
        HistorialMedicoDTO historial = new HistorialMedicoDTO();
        historial.setNombreCompleto("Paciente Test");

        assertDoesNotThrow(() ->
                informeMedicoService.generarInformePdf(
                        historial,
                        null
                )
        );
    }
}