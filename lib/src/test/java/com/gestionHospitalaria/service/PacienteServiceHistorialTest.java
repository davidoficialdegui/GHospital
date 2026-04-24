package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceHistorialTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Ana");
        paciente.setApellido1("Martínez");
        paciente.setApellido2("Ruiz");
        paciente.setGrupoSanguineo("A+");
        paciente.setAltura(1.65);
        paciente.setPeso(60.0);
        paciente.setAlergias("Polen");
        paciente.setEnfermedadesPrevias("Ninguna");
        paciente.setMedicacionActual("Ibuprofeno");
        paciente.setAntecedentesFamiliares("Diabetes");
        paciente.setObservacionesMedicas("Sin observaciones");
    }

    @Test
    void obtenerHistorial_pacienteExiste_devuelveDTO() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        HistorialMedicoDTO resultado = pacienteService.obtenerHistorial(1L);

        assertNotNull(resultado);
        assertEquals("Ana Martínez Ruiz", resultado.getNombreCompleto());
        assertEquals("A+", resultado.getGrupoSanguineo());
        assertEquals(1.65, resultado.getAltura());
        assertEquals(60.0, resultado.getPeso());
        assertEquals("Polen", resultado.getAlergias());
        assertEquals("Ibuprofeno", resultado.getMedicacionActual());
        assertEquals("Diabetes", resultado.getAntecedentesFamiliares());
    }

    @Test
    void obtenerHistorial_pacienteNoExiste_lanzaExcepcion() {
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> pacienteService.obtenerHistorial(999L));
        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
    }

    @Test
    void obtenerHistorial_camposOpcionalosNull_noLanzaExcepcion() {
        paciente.setAlergias(null);
        paciente.setMedicacionActual(null);
        paciente.setGrupoSanguineo(null);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        HistorialMedicoDTO resultado = pacienteService.obtenerHistorial(1L);

        assertNotNull(resultado);
        assertNull(resultado.getAlergias());
        assertNull(resultado.getMedicacionActual());
        assertNull(resultado.getGrupoSanguineo());
    }

    @Test
    void obtenerHistorial_nombreCompletoConcat_esCorrector() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        HistorialMedicoDTO resultado = pacienteService.obtenerHistorial(1L);

        // Verifica que el nombre completo concatena correctamente los tres campos
        String esperado = paciente.getNombre() + " " + paciente.getApellido1() + " " + paciente.getApellido2();
        assertEquals(esperado, resultado.getNombreCompleto());
    }
}