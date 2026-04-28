package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.EditarMedicoDTO;
import com.gestionHospitalaria.dto.EditarPacienteDTO;
import com.gestionHospitalaria.dto.EditarRecepcionistaDTO;
import com.gestionHospitalaria.entity.Cita;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.entity.Recepcionista;
import com.gestionHospitalaria.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock private PacienteRepository      pacienteRepository;
    @Mock private MedicoRepository        medicoRepository;
    @Mock private RecepcionistaRepository recepcionistaRepository;
    @Mock private CitaRepository          citaRepository;
    @Mock private DiagnosticoRepository   diagnosticoRepository;

    @InjectMocks
    private AdminService adminService;

    private Paciente      paciente;
    private Medico        medico;
    private Recepcionista recepcionista;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Ana");
        paciente.setApellido1("Martínez");
        paciente.setApellido2("Ruiz");
        paciente.setDni("12345678A");
        paciente.setEmail("ana@email.com");
        paciente.setRol(Paciente.Rol.PACIENTE);

        medico = new Medico();
        medico.setId(1L);
        medico.setNombre("Carlos");
        medico.setApellido1("García");
        medico.setDni("11111111A");
        medico.setEmail("carlos@hospital.com");
        medico.setEspecialidad("Cardiología");
        medico.setRol(Paciente.Rol.MEDICO);

        recepcionista = new Recepcionista();
        recepcionista.setId(1L);
        recepcionista.setNombre("María");
        recepcionista.setApellido1("Torres");
        recepcionista.setDni("44444444D");
        recepcionista.setEmail("recepcion@hospital.com");
        recepcionista.setRol(Paciente.Rol.RECEPCIONISTA);
    }

    // ══════════════════════════════════════════════════════════════
    // TANDA 1 — EDITAR (tests 1-5)
    // ══════════════════════════════════════════════════════════════

    /** Test 1: editar paciente existente actualiza nombre y email */
    @Test
    void editarPaciente_correcto_actualizaNombreYEmail() {
        EditarPacienteDTO dto = new EditarPacienteDTO();
        dto.setNombre("Ana Nueva");
        dto.setApellido1("Martínez");
        dto.setApellido2("Ruiz");
        dto.setDni("12345678A");
        dto.setEmail("ana_nueva@email.com");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        adminService.editarPaciente(1L, dto);

        assertEquals("Ana Nueva",          paciente.getNombre());
        assertEquals("ana_nueva@email.com", paciente.getEmail());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    /** Test 2: editar paciente que no existe lanza excepción */
    @Test
    void editarPaciente_noExiste_lanzaExcepcion() {
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.editarPaciente(999L, new EditarPacienteDTO()));

        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
        verify(pacienteRepository, never()).save(any());
    }

    /** Test 3: editar médico existente actualiza especialidad */
    @Test
    void editarMedico_correcto_actualizaEspecialidad() {
        EditarMedicoDTO dto = new EditarMedicoDTO();
        dto.setNombre("Carlos");
        dto.setApellido1("García");
        dto.setDni("11111111A");
        dto.setEmail("carlos@hospital.com");
        dto.setEspecialidad("Neurología");

        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        adminService.editarMedico(1L, dto);

        assertEquals("Neurología", medico.getEspecialidad());
        verify(medicoRepository, times(1)).save(medico);
    }

    /** Test 4: editar médico que no existe lanza excepción */
    @Test
    void editarMedico_noExiste_lanzaExcepcion() {
        when(medicoRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.editarMedico(999L, new EditarMedicoDTO()));

        assertTrue(ex.getMessage().contains("Médico no encontrado"));
        verify(medicoRepository, never()).save(any());
    }

    /** Test 5: editar recepcionista existente actualiza departamento */
    @Test
    void editarRecepcionista_correcto_actualizaDepartamento() {
        EditarRecepcionistaDTO dto = new EditarRecepcionistaDTO();
        dto.setNombre("María");
        dto.setApellido1("Torres");
        dto.setDni("44444444D");
        dto.setEmail("recepcion@hospital.com");
        dto.setDepartamentoAsignado("UCI");

        when(recepcionistaRepository.findById(1L)).thenReturn(Optional.of(recepcionista));
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenReturn(recepcionista);

        adminService.editarRecepcionista(1L, dto);

        assertEquals("UCI", recepcionista.getDepartamentoAsignado());
        verify(recepcionistaRepository, times(1)).save(recepcionista);
    }

    // ══════════════════════════════════════════════════════════════
    // TANDA 2 — EDITAR (test 6) + BORRAR (tests 7-10)
    // ══════════════════════════════════════════════════════════════

    /** Test 6: editar recepcionista que no existe lanza excepción */
    @Test
    void editarRecepcionista_noExiste_lanzaExcepcion() {
        when(recepcionistaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.editarRecepcionista(999L, new EditarRecepcionistaDTO()));

        assertTrue(ex.getMessage().contains("Recepcionista no encontrado"));
        verify(recepcionistaRepository, never()).save(any());
    }

    /** Test 7: eliminar paciente existente llama a deleteById */
    @Test
    void eliminarPaciente_correcto_llamaDeleteById() {
        when(pacienteRepository.existsById(1L)).thenReturn(true);

        adminService.eliminarPaciente(1L);

        verify(pacienteRepository, times(1)).deleteById(1L);
    }

    /** Test 8: eliminar paciente que no existe lanza excepción */
    @Test
    void eliminarPaciente_noExiste_lanzaExcepcion() {
        when(pacienteRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.eliminarPaciente(999L));

        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
        verify(pacienteRepository, never()).deleteById(any());
    }

    /** Test 9: eliminar médico existente llama a deleteById */
    @Test
    void eliminarMedico_correcto_llamaDeleteById() {
        when(medicoRepository.existsById(1L)).thenReturn(true);

        adminService.eliminarMedico(1L);

        verify(medicoRepository, times(1)).deleteById(1L);
    }

    /** Test 10: eliminar médico que no existe lanza excepción */
    @Test
    void eliminarMedico_noExiste_lanzaExcepcion() {
        when(medicoRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.eliminarMedico(999L));

        assertTrue(ex.getMessage().contains("Médico no encontrado"));
        verify(medicoRepository, never()).deleteById(any());
    }

    // ══════════════════════════════════════════════════════════════
    // TANDA 3 — BORRAR (tests 11-12) + ASIGNAR ROL (tests 13-15)
    // ══════════════════════════════════════════════════════════════

    /** Test 11: eliminar recepcionista existente llama a deleteById */
    @Test
    void eliminarRecepcionista_correcto_llamaDeleteById() {
        when(recepcionistaRepository.existsById(1L)).thenReturn(true);

        adminService.eliminarRecepcionista(1L);

        verify(recepcionistaRepository, times(1)).deleteById(1L);
    }

    /** Test 12: eliminar recepcionista que no existe lanza excepción */
    @Test
    void eliminarRecepcionista_noExiste_lanzaExcepcion() {
        when(recepcionistaRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.eliminarRecepcionista(999L));

        assertTrue(ex.getMessage().contains("Recepcionista no encontrado"));
        verify(recepcionistaRepository, never()).deleteById(any());
    }

    /** Test 13: asignar rol MEDICO a paciente → se guarda con nuevo rol */
    @Test
    void asignarRolPaciente_correcto_cambiaRol() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        adminService.asignarRolPaciente(1L, "MEDICO");

        assertEquals(Paciente.Rol.MEDICO, paciente.getRol());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    /** Test 14: asignar rol a paciente que no existe lanza excepción */
    @Test
    void asignarRolPaciente_noExiste_lanzaExcepcion() {
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.asignarRolPaciente(999L, "MEDICO"));

        assertTrue(ex.getMessage().contains("Paciente no encontrado"));
        verify(pacienteRepository, never()).save(any());
    }

    /** Test 15: asignar rol ADMIN a médico → se guarda con rol ADMIN */
    @Test
    void asignarRolMedico_correcto_cambiaRolAAdmin() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        adminService.asignarRolMedico(1L, "ADMIN");

        assertEquals(Paciente.Rol.ADMIN, medico.getRol());
        verify(medicoRepository, times(1)).save(medico);
    }

    // ══════════════════════════════════════════════════════════════
    // TANDA 4 — ASIGNAR ROL (tests 16-20)
    // ══════════════════════════════════════════════════════════════

    /** Test 16: asignar rol a médico que no existe lanza excepción */
    @Test
    void asignarRolMedico_noExiste_lanzaExcepcion() {
        when(medicoRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.asignarRolMedico(999L, "ADMIN"));

        assertTrue(ex.getMessage().contains("Médico no encontrado"));
        verify(medicoRepository, never()).save(any());
    }

    /** Test 17: asignar rol PACIENTE a recepcionista → rol cambia */
    @Test
    void asignarRolRecepcionista_correcto_cambiaRol() {
        when(recepcionistaRepository.findById(1L)).thenReturn(Optional.of(recepcionista));
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenReturn(recepcionista);

        adminService.asignarRolRecepcionista(1L, "PACIENTE");

        assertEquals(Paciente.Rol.PACIENTE, recepcionista.getRol());
        verify(recepcionistaRepository, times(1)).save(recepcionista);
    }

    /** Test 18: asignar rol a recepcionista que no existe lanza excepción */
    @Test
    void asignarRolRecepcionista_noExiste_lanzaExcepcion() {
        when(recepcionistaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminService.asignarRolRecepcionista(999L, "ADMIN"));

        assertTrue(ex.getMessage().contains("Recepcionista no encontrado"));
        verify(recepcionistaRepository, never()).save(any());
    }

    /** Test 19: asignar rol ADMIN a paciente (caso especial) → se guarda */
    @Test
    void asignarRolPaciente_aAdmin_seGuardaCorrectamente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        adminService.asignarRolPaciente(1L, "ADMIN");

        assertEquals(Paciente.Rol.ADMIN, paciente.getRol());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    /** Test 20: asignar rol RECEPCIONISTA a médico → rol cambia correctamente */
    @Test
    void asignarRolMedico_aRecepcionista_cambiaRol() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        adminService.asignarRolMedico(1L, "RECEPCIONISTA");

        assertEquals(Paciente.Rol.RECEPCIONISTA, medico.getRol());
        verify(medicoRepository, times(1)).save(medico);
    }

    // ══════════════════════════════════════════════════════════════
    // TANDA 5 — ESTADÍSTICAS (tests 21-25)
    // ══════════════════════════════════════════════════════════════

    /** Test 21: obtenerEstadisticas devuelve mapa con todos los campos */
    @Test
    void obtenerEstadisticas_devuelveTodosLosCampos() {
        when(pacienteRepository.countByRol(Paciente.Rol.PACIENTE)).thenReturn(2L);
        when(medicoRepository.countByRol(Paciente.Rol.PACIENTE)).thenReturn(0L);
        when(recepcionistaRepository.countByRol(Paciente.Rol.PACIENTE)).thenReturn(0L);
        when(pacienteRepository.countByRol(Paciente.Rol.MEDICO)).thenReturn(0L);
        when(medicoRepository.countByRol(Paciente.Rol.MEDICO)).thenReturn(2L);
        when(recepcionistaRepository.countByRol(Paciente.Rol.MEDICO)).thenReturn(0L);
        when(pacienteRepository.countByRol(Paciente.Rol.RECEPCIONISTA)).thenReturn(0L);
        when(medicoRepository.countByRol(Paciente.Rol.RECEPCIONISTA)).thenReturn(0L);
        when(recepcionistaRepository.countByRol(Paciente.Rol.RECEPCIONISTA)).thenReturn(1L);
        when(citaRepository.count()).thenReturn(5L);
        when(citaRepository.countByEstado(Cita.EstadoCita.PENDIENTE)).thenReturn(2L);
        when(citaRepository.countByEstado(Cita.EstadoCita.CONFIRMADA)).thenReturn(1L);
        when(citaRepository.countByEstado(Cita.EstadoCita.CANCELADA)).thenReturn(1L);
        when(citaRepository.countByEstado(Cita.EstadoCita.REALIZADA)).thenReturn(1L);
        when(diagnosticoRepository.count()).thenReturn(3L);

        Map<String, Long> stats = adminService.obtenerEstadisticas();

        assertNotNull(stats);
        assertTrue(stats.containsKey("totalPacientes"));
        assertTrue(stats.containsKey("totalMedicos"));
        assertTrue(stats.containsKey("totalRecepcionistas"));
        assertTrue(stats.containsKey("totalCitas"));
        assertTrue(stats.containsKey("citasPendientes"));
        assertTrue(stats.containsKey("citasConfirmadas"));
        assertTrue(stats.containsKey("citasCanceladas"));
        assertTrue(stats.containsKey("citasRealizadas"));
        assertTrue(stats.containsKey("totalDiagnosticos"));
    }

    /** Test 22: obtenerEstadisticas cuenta correctamente pacientes por rol */
    @Test
    void obtenerEstadisticas_totalPacientes_esSumaDeLasTresTablasConRolPaciente() {
        // Paciente: 2 en tabla pacientes, 1 en tabla médicos (médico con rol PACIENTE), 0 en recepcionistas
        when(pacienteRepository.countByRol(Paciente.Rol.PACIENTE)).thenReturn(2L);
        when(pacienteRepository.countByRol(Paciente.Rol.MEDICO)).thenReturn(0L);
        when(pacienteRepository.countByRol(Paciente.Rol.RECEPCIONISTA)).thenReturn(0L);
        when(medicoRepository.countByRol(Paciente.Rol.PACIENTE)).thenReturn(1L);
        when(medicoRepository.countByRol(Paciente.Rol.MEDICO)).thenReturn(0L);
        when(medicoRepository.countByRol(Paciente.Rol.RECEPCIONISTA)).thenReturn(0L);
        when(recepcionistaRepository.countByRol(Paciente.Rol.PACIENTE)).thenReturn(0L);
        when(recepcionistaRepository.countByRol(Paciente.Rol.MEDICO)).thenReturn(0L);
        when(recepcionistaRepository.countByRol(Paciente.Rol.RECEPCIONISTA)).thenReturn(0L);
        when(citaRepository.count()).thenReturn(0L);
        when(citaRepository.countByEstado(any())).thenReturn(0L);
        when(diagnosticoRepository.count()).thenReturn(0L);

        Map<String, Long> stats = adminService.obtenerEstadisticas();

        assertEquals(3L, stats.get("totalPacientes")); // 2 + 1 + 0
    }

    /** Test 23: obtenerEstadisticas totalCitas refleja count del repositorio */
    @Test
    void obtenerEstadisticas_totalCitas_correcto() {
        when(pacienteRepository.countByRol(any())).thenReturn(0L);
        when(medicoRepository.countByRol(any())).thenReturn(0L);
        when(recepcionistaRepository.countByRol(any())).thenReturn(0L);
        when(citaRepository.count()).thenReturn(7L);
        when(citaRepository.countByEstado(any())).thenReturn(0L);
        when(diagnosticoRepository.count()).thenReturn(0L);

        Map<String, Long> stats = adminService.obtenerEstadisticas();

        assertEquals(7L, stats.get("totalCitas"));
    }

    /** Test 24: obtenerEstadisticas cuenta citas pendientes correctamente */
    @Test
    void obtenerEstadisticas_citasPendientes_correcto() {
        when(pacienteRepository.countByRol(any())).thenReturn(0L);
        when(medicoRepository.countByRol(any())).thenReturn(0L);
        when(recepcionistaRepository.countByRol(any())).thenReturn(0L);
        when(citaRepository.count()).thenReturn(0L);
        when(citaRepository.countByEstado(Cita.EstadoCita.PENDIENTE)).thenReturn(4L);
        when(citaRepository.countByEstado(Cita.EstadoCita.CONFIRMADA)).thenReturn(0L);
        when(citaRepository.countByEstado(Cita.EstadoCita.CANCELADA)).thenReturn(0L);
        when(citaRepository.countByEstado(Cita.EstadoCita.REALIZADA)).thenReturn(0L);
        when(diagnosticoRepository.count()).thenReturn(0L);

        Map<String, Long> stats = adminService.obtenerEstadisticas();

        assertEquals(4L, stats.get("citasPendientes"));
    }

    /** Test 25: obtenerEstadisticas totalDiagnosticos refleja count */
    @Test
    void obtenerEstadisticas_totalDiagnosticos_correcto() {
        when(pacienteRepository.countByRol(any())).thenReturn(0L);
        when(medicoRepository.countByRol(any())).thenReturn(0L);
        when(recepcionistaRepository.countByRol(any())).thenReturn(0L);
        when(citaRepository.count()).thenReturn(0L);
        when(citaRepository.countByEstado(any())).thenReturn(0L);
        when(diagnosticoRepository.count()).thenReturn(9L);

        Map<String, Long> stats = adminService.obtenerEstadisticas();

        assertEquals(9L, stats.get("totalDiagnosticos"));
    }

    // ══════════════════════════════════════════════════════════════
    // TANDA 6 — ESTADÍSTICAS + LISTAR (tests 26-27)
    // ══════════════════════════════════════════════════════════════

    /** Test 26: obtenerEstadisticas cuenta recepcionistas por rol, no por tabla */
    @Test
    void obtenerEstadisticas_totalRecepcionistas_soloContaRolRecepcionista() {
        // David (ADMIN) está en tabla recepcionistas pero NO debe contar
        when(pacienteRepository.countByRol(any())).thenReturn(0L);
        when(medicoRepository.countByRol(any())).thenReturn(0L);
        when(recepcionistaRepository.countByRol(any())).thenReturn(0L);
        when(recepcionistaRepository.countByRol(Paciente.Rol.RECEPCIONISTA)).thenReturn(1L); // solo María
        when(citaRepository.count()).thenReturn(0L);
        when(citaRepository.countByEstado(any())).thenReturn(0L);
        when(diagnosticoRepository.count()).thenReturn(0L);

        Map<String, Long> stats = adminService.obtenerEstadisticas();

        assertEquals(1L, stats.get("totalRecepcionistas")); // David (ADMIN) no cuenta
    }

    /** Test 27: listarPacientes devuelve todos los pacientes de la tabla */
    @Test
    void listarPacientes_devuelveListaCompleta() {
        Paciente p2 = new Paciente();
        p2.setId(2L);
        p2.setNombre("Pedro");
        p2.setApellido1("López");

        when(pacienteRepository.findAll()).thenReturn(List.of(paciente, p2));

        var resultado = adminService.listarPacientes();

        assertEquals(2, resultado.size());
        assertEquals("Ana",   resultado.get(0).getNombre());
        assertEquals("Pedro", resultado.get(1).getNombre());
        verify(pacienteRepository, times(1)).findAll();
    }
}
