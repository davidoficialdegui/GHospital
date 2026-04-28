package com.gestionHospitalaria.performance;

import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import com.gestionHospitalaria.service.InformeMedicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de rendimiento del sistema.
 * Verifican que las operaciones críticas se completan
 * dentro de los tiempos máximos aceptables.
 *
 * Separados de los tests unitarios mediante @Tag("performance").
 * Ejecutar con: gradlew.bat performanceTest
 */
@Tag("performance")
class RendimientoServiceTest {

    private InformeMedicoService informeMedicoService;

    @BeforeEach
    void setUp() {
        informeMedicoService = new InformeMedicoService();
    }

    // ── Generación de PDF ────────────────────────────────────────

    @Test
    void generarPdf_sinDiagnosticos_menosDe500ms() {
        HistorialMedicoDTO historial = historialBase("Ana Martínez");

        long inicio = System.currentTimeMillis();
        informeMedicoService.generarInformePdf(historial, List.of());
        long tiempo = System.currentTimeMillis() - inicio;

        assertTrue(tiempo < 500,
                "Generar PDF sin diagnósticos tardó " + tiempo + "ms (límite: 500ms)");
    }

    @Test
    void generarPdf_con10Diagnosticos_menosDe1000ms() {
        HistorialMedicoDTO historial = historialBase("Pedro López");
        List<DiagnosticoDTO> diagnosticos = generarDiagnosticos(10);

        long inicio = System.currentTimeMillis();
        informeMedicoService.generarInformePdf(historial, diagnosticos);
        long tiempo = System.currentTimeMillis() - inicio;

        assertTrue(tiempo < 1000,
                "Generar PDF con 10 diagnósticos tardó " + tiempo + "ms (límite: 1000ms)");
    }

    @Test
    void generarPdf_con50Diagnosticos_menosDe3000ms() {
        HistorialMedicoDTO historial = historialBase("Carlos García");
        List<DiagnosticoDTO> diagnosticos = generarDiagnosticos(50);

        long inicio = System.currentTimeMillis();
        informeMedicoService.generarInformePdf(historial, diagnosticos);
        long tiempo = System.currentTimeMillis() - inicio;

        assertTrue(tiempo < 3000,
                "Generar PDF con 50 diagnósticos tardó " + tiempo + "ms (límite: 3000ms)");
    }

    // ── Generación masiva (carga) ────────────────────────────────

    @Test
    void generarPdf_100VecesConsecutivas_menosDe10000ms() {
        HistorialMedicoDTO historial = historialBase("Paciente Carga");
        List<DiagnosticoDTO> diagnosticos = generarDiagnosticos(3);

        long inicio = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            informeMedicoService.generarInformePdf(historial, diagnosticos);
        }
        long tiempo = System.currentTimeMillis() - inicio;

        assertTrue(tiempo < 10000,
                "100 generaciones de PDF tardaron " + tiempo + "ms (límite: 10000ms)");
    }

    @Test
    void generarPdf_tiempoMedioMenorDe200ms() {
        HistorialMedicoDTO historial = historialBase("Paciente Promedio");
        List<DiagnosticoDTO> diagnosticos = generarDiagnosticos(5);
        int iteraciones = 20;

        long inicio = System.currentTimeMillis();
        for (int i = 0; i < iteraciones; i++) {
            informeMedicoService.generarInformePdf(historial, diagnosticos);
        }
        long total = System.currentTimeMillis() - inicio;
        long promedio = total / iteraciones;

        assertTrue(promedio < 200,
                "Tiempo medio de generación PDF: " + promedio + "ms (límite: 200ms)");
    }

    // ── Helpers ──────────────────────────────────────────────────

    private HistorialMedicoDTO historialBase(String nombre) {
        HistorialMedicoDTO h = new HistorialMedicoDTO();
        h.setNombreCompleto(nombre);
        h.setGrupoSanguineo("A+");
        h.setAltura(1.75);
        h.setPeso(70.0);
        h.setAlergias("Polen");
        h.setMedicacionActual("Ibuprofeno");
        h.setEnfermedadesPrevias("Ninguna");
        h.setAntecedentesFamiliares("Hipertensión");
        h.setObservacionesMedicas("Revisión anual recomendada");
        return h;
    }

    private List<DiagnosticoDTO> generarDiagnosticos(int cantidad) {
        List<DiagnosticoDTO> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            DiagnosticoDTO d = new DiagnosticoDTO();
            d.setDescripcion("Diagnóstico número " + (i + 1));
            d.setTratamiento("Tratamiento " + (i + 1));
            d.setObservaciones("Observación " + (i + 1));
            d.setMedicoNombre("Dr. García");
            d.setFechaDiagnostico(LocalDateTime.now().minusDays(i));
            lista.add(d);
        }
        return lista;
    }
}
