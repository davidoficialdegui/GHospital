package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CrearUsuarioAdminDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para la creación de usuarios por parte del administrador.
 * Historia de usuario: Como administrador, quiero poder crear usuarios.
 */
@ExtendWith(MockitoExtension.class)
class AdminServiceCrearUsuarioTest {

    @Mock private PacienteRepository      pacienteRepository;
    @Mock private MedicoRepository        medicoRepository;
    @Mock private RecepcionistaRepository recepcionistaRepository;
    @Mock private CitaRepository          citaRepository;
    @Mock private DiagnosticoRepository   diagnosticoRepository;
    @Mock private PasswordEncoder         passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    private CrearUsuarioAdminDTO dto;

    @BeforeEach
    void setUp() {
        dto = new CrearUsuarioAdminDTO();
        dto.setNombre("Juan");
        dto.setApellido1("Pérez");
        dto.setApellido2("López");
        dto.setDni("11223344B");
        dto.setEmail("juan@hospital.com");
        dto.setPassword("pass123");

        when(passwordEncoder.encode(anyString())).thenReturn("$2a$encoded");
    }

    // ── Crear PACIENTE ───────────────────────────────────────────

    @Test
    void crearUsuario_tipoPaciente_guardaEnPacienteRepository() {
        dto.setTipo("PACIENTE");
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> {
            Paciente p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        adminService.crearUsuario(dto);

        verify(pacienteRepository).save(any(Paciente.class));
        verify(medicoRepository, never()).save(any());
        verify(recepcionistaRepository, never()).save(any());
    }

    @Test
    void crearUsuario_tipoPaciente_passwordEncriptada() {
        dto.setTipo("PACIENTE");
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> inv.getArgument(0));

        adminService.crearUsuario(dto);

        verify(passwordEncoder).encode("pass123");
        verify(pacienteRepository).save(argThat(p ->
                p.getPassword().equals("$2a$encoded")));
    }

    @Test
    void crearUsuario_tipoPaciente_rolAsignadoCorrecto() {
        dto.setTipo("PACIENTE");
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> inv.getArgument(0));

        adminService.crearUsuario(dto);

        verify(pacienteRepository).save(argThat(p ->
                p.getRol() == Paciente.Rol.PACIENTE));
    }

    // ── Crear MEDICO ─────────────────────────────────────────────

    @Test
    void crearUsuario_tipoMedico_guardaEnMedicoRepository() {
        dto.setTipo("MEDICO");
        dto.setEspecialidad("Cardiología");
        when(medicoRepository.save(any(Medico.class))).thenAnswer(inv -> inv.getArgument(0));

        adminService.crearUsuario(dto);

        verify(medicoRepository).save(any(Medico.class));
        verify(pacienteRepository, never()).save(any());
        verify(recepcionistaRepository, never()).save(any());
    }

    @Test
    void crearUsuario_tipoMedico_especialidadAsignada() {
        dto.setTipo("MEDICO");
        dto.setEspecialidad("Neurología");
        when(medicoRepository.save(any(Medico.class))).thenAnswer(inv -> inv.getArgument(0));

        adminService.crearUsuario(dto);

        verify(medicoRepository).save(argThat(m ->
                "Neurología".equals(m.getEspecialidad())));
    }

    @Test
    void crearUsuario_tipoMedicoSinEspecialidad_usaValorPorDefecto() {
        dto.setTipo("MEDICO");
        dto.setEspecialidad(null);
        when(medicoRepository.save(any(Medico.class))).thenAnswer(inv -> inv.getArgument(0));

        adminService.crearUsuario(dto);

        verify(medicoRepository).save(argThat(m ->
                "General".equals(m.getEspecialidad())));
    }

    @Test
    void crearUsuario_tipoMedico_rolAsignadoCorrecto() {
        dto.setTipo("MEDICO");
        dto.setEspecialidad("Pediatría");
        when(medicoRepository.save(any(Medico.class))).thenAnswer(inv -> inv.getArgument(0));

        adminService.crearUsuario(dto);

        verify(medicoRepository).save(argThat(m ->
                m.getRol() == Paciente.Rol.MEDICO));
    }

    // ── Crear RECEPCIONISTA ──────────────────────────────────────

    @Test
    void crearUsuario_tipoRecepcionista_guardaEnRecepcionistaRepository() {
        dto.setTipo("RECEPCIONISTA");
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenAnswer(inv -> inv.getArgument(0));

        adminService.crearUsuario(dto);

        verify(recepcionistaRepository).save(any(Recepcionista.class));
        verify(pacienteRepository, never()).save(any());
        verify(medicoRepository, never()).save(any());
    }

    @Test
    void crearUsuario_tipoRecepcionista_rolAsignadoCorrecto() {
        dto.setTipo("RECEPCIONISTA");
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenAnswer(inv -> inv.getArgument(0));

        adminService.crearUsuario(dto);

        verify(recepcionistaRepository).save(argThat(r ->
                r.getRol() == Paciente.Rol.RECEPCIONISTA));
    }

    @Test
    void crearUsuario_tipoRecepcionista_datosPersonalesGuardados() {
        dto.setTipo("RECEPCIONISTA");
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenAnswer(inv -> inv.getArgument(0));

        adminService.crearUsuario(dto);

        verify(recepcionistaRepository).save(argThat(r ->
                "Juan".equals(r.getNombre()) &&
                "Pérez".equals(r.getApellido1()) &&
                "juan@hospital.com".equals(r.getEmail())));
    }
}
