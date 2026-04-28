package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.*;
import com.gestionHospitalaria.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de cobertura para entidades y DTOs.
 * Verifica que todos los getters, setters y constructores funcionan correctamente.
 */
class EntidadesYDtosTest {

    // ── ENTIDAD PACIENTE ─────────────────────────────────────────

    @Test
    void paciente_gettersYSetters_funcionanCorrectamente() {
        Paciente p = new Paciente();
        p.setId(1L);
        p.setNombre("Ana");
        p.setApellido1("Martínez");
        p.setApellido2("López");
        p.setDni("12345678A");
        p.setEmail("ana@email.com");
        p.setPassword("pass");
        p.setTelefono("600000000");
        p.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        p.setRol(Paciente.Rol.PACIENTE);
        p.setGrupoSanguineo("A+");
        p.setAltura(1.65);
        p.setPeso(60.0);
        p.setAlergias("Polen");
        p.setMedicacionActual("Ibuprofeno");
        p.setEnfermedadesPrevias("Ninguna");
        p.setAntecedentesFamiliares("Hipertensión");
        p.setObservacionesMedicas("Revisión anual");
        p.setDireccion("Calle Mayor 1");
        p.setCiudad("Madrid");
        p.setProvincia("Madrid");
        p.setCodigoPostal("28001");
        p.setPais("España");
        p.setNacionalidad("Española");
        p.setSexo("F");
        p.setGenero("Femenino");
        p.setEstadoCivil("Soltera");
        p.setOcupacion("Enfermera");
        p.setAseguradora("Mapfre");
        p.setNumeroPoliza("POL001");
        p.setNombreContactoEmergencia("Luis");
        p.setParentescoContactoEmergencia("Hermano");
        p.setTelefonoEmergencia("611111111");
        p.setNumeroHistoriaClinica("HC001");
        p.setActivo(true);
        p.setTieneSeguroPrivado(false);
        p.setConsentimientoProteccionDatos(true);
        p.setConsentimientoTratamientoDatos(true);
        p.setFechaAlta(LocalDate.now());
        p.setFechaUltimaRevision(LocalDate.now());
        p.setFechaActualizacion(LocalDateTime.now());
        p.setCitas(new java.util.ArrayList<>());

        assertEquals(1L, p.getId());
        assertEquals("Ana", p.getNombre());
        assertEquals("Martínez", p.getApellido1());
        assertEquals("López", p.getApellido2());
        assertEquals("12345678A", p.getDni());
        assertEquals("ana@email.com", p.getEmail());
        assertEquals("pass", p.getPassword());
        assertEquals("600000000", p.getTelefono());
        assertEquals(Paciente.Rol.PACIENTE, p.getRol());
        assertEquals("A+", p.getGrupoSanguineo());
        assertEquals(1.65, p.getAltura());
        assertEquals(60.0, p.getPeso());
        assertTrue(p.getActivo());
        assertFalse(p.getTieneSeguroPrivado());
        assertTrue(p.getConsentimientoProteccionDatos());
        assertNotNull(p.getCitas());
    }

    @Test
    void paciente_prePersist_inicializaValoresPorDefecto() {
        Paciente p = new Paciente();
        p.prePersist();
        assertTrue(p.getActivo());
        assertFalse(p.getTieneSeguroPrivado());
        assertFalse(p.getConsentimientoProteccionDatos());
        assertFalse(p.getConsentimientoTratamientoDatos());
        assertEquals(Paciente.Rol.PACIENTE, p.getRol());
        assertNotNull(p.getFechaCreacion());
    }

    @Test
    void paciente_preUpdate_actualizaFecha() {
        Paciente p = new Paciente();
        p.preUpdate();
        assertNotNull(p.getFechaActualizacion());
    }

    @Test
    void paciente_roles_todosDisponibles() {
        assertEquals(4, Paciente.Rol.values().length);
        assertNotNull(Paciente.Rol.valueOf("PACIENTE"));
        assertNotNull(Paciente.Rol.valueOf("MEDICO"));
        assertNotNull(Paciente.Rol.valueOf("ADMIN"));
        assertNotNull(Paciente.Rol.valueOf("RECEPCIONISTA"));
    }

    // ── ENTIDAD MEDICO ───────────────────────────────────────────

    @Test
    void medico_gettersYSetters_funcionanCorrectamente() {
        Medico m = new Medico();
        m.setId(1L);
        m.setNombre("Carlos");
        m.setApellido1("García");
        m.setApellido2("Ruiz");
        m.setDni("87654321B");
        m.setEmail("carlos@hospital.com");
        m.setPassword("pass");
        m.setTelefono("622222222");
        m.setEspecialidad("Cardiología");
        m.setSubespecialidad("Arritmias");
        m.setDepartamento("Cardiología");
        m.setAniosExperiencia(10);
        m.setFechaIncorporacion(LocalDate.of(2015, 1, 1));
        m.setFechaNacimiento(LocalDate.of(1980, 5, 10));
        m.setTurno("MAÑANA");
        m.setDescripcionProfesional("Cardiólogo especialista");
        m.setRol(Paciente.Rol.MEDICO);
        m.setActivo(true);
        m.setDireccion("Calle Hospital 1");
        m.setCiudad("Barcelona");
        m.setProvincia("Barcelona");
        m.setCodigoPostal("08001");
        m.setSexo("M");
        m.setNumeroColegiado("COL12345");
        m.setFechaActualizacion(LocalDateTime.now());
        m.setCitas(new java.util.ArrayList<>());

        assertEquals(1L, m.getId());
        assertEquals("Carlos", m.getNombre());
        assertEquals("García", m.getApellido1());
        assertEquals("Cardiología", m.getEspecialidad());
        assertEquals(Paciente.Rol.MEDICO, m.getRol());
        assertTrue(m.getActivo());
        assertNotNull(m.getCitas());
    }

    @Test
    void medico_prePersist_inicializaValores() {
        Medico m = new Medico();
        m.prePersist();
        assertTrue(m.getActivo());
        assertEquals(Paciente.Rol.MEDICO, m.getRol());
        assertNotNull(m.getFechaCreacion());
    }

    @Test
    void medico_preUpdate_actualizaFecha() {
        Medico m = new Medico();
        m.preUpdate();
        assertNotNull(m.getFechaActualizacion());
    }

    // ── ENTIDAD CITA ─────────────────────────────────────────────

    @Test
    void cita_gettersYSetters_funcionanCorrectamente() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        Medico medico = new Medico();
        medico.setId(1L);

        Cita cita = new Cita();
        cita.setId(1L);
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaHora(LocalDateTime.now());
        cita.setEstado(Cita.EstadoCita.PENDIENTE);
        cita.setMotivo("Revisión");
        cita.setEspecialidad("Cardiología");
        cita.setObservaciones("Sin notas");
        cita.setFechaActualizacion(LocalDateTime.now());

        assertEquals(1L, cita.getId());
        assertEquals(paciente, cita.getPaciente());
        assertEquals(medico, cita.getMedico());
        assertEquals(Cita.EstadoCita.PENDIENTE, cita.getEstado());
        assertEquals("Revisión", cita.getMotivo());
        assertEquals("Cardiología", cita.getEspecialidad());
        assertNotNull(cita.getFechaHora());
    }

    @Test
    void cita_estados_todosDisponibles() {
        assertEquals(4, Cita.EstadoCita.values().length);
        assertNotNull(Cita.EstadoCita.valueOf("PENDIENTE"));
        assertNotNull(Cita.EstadoCita.valueOf("CONFIRMADA"));
        assertNotNull(Cita.EstadoCita.valueOf("CANCELADA"));
        assertNotNull(Cita.EstadoCita.valueOf("REALIZADA"));
    }

    // ── ENTIDAD DIAGNOSTICO ──────────────────────────────────────

    @Test
    void diagnostico_gettersYSetters_funcionanCorrectamente() {
        Diagnostico d = new Diagnostico();
        Paciente p = new Paciente(); p.setId(1L);
        Medico m = new Medico(); m.setId(1L);

        d.setId(1L);
        d.setPaciente(p);
        d.setMedico(m);
        d.setDescripcion("Hipertensión");
        d.setTratamiento("Dieta");
        d.setObservaciones("Revisión en 3 meses");
        d.setFechaDiagnostico(LocalDateTime.now());

        assertEquals(1L, d.getId());
        assertEquals("Hipertensión", d.getDescripcion());
        assertEquals("Dieta", d.getTratamiento());
        assertEquals("Revisión en 3 meses", d.getObservaciones());
        assertEquals(p, d.getPaciente());
        assertEquals(m, d.getMedico());
    }

    // ── DTO: CitaDTO ─────────────────────────────────────────────

    @Test
    void citaDTO_gettersYSetters_funcionanCorrectamente() {
        CitaDTO dto = new CitaDTO();
        dto.setId(1L);
        dto.setPacienteId(2L);
        dto.setPacienteNombre("Ana Martínez");
        dto.setFechaHora(LocalDateTime.now());
        dto.setEstado("PENDIENTE");
        dto.setMotivo("Revisión");
        dto.setEspecialidad("Cardiología");
        dto.setMedicoNombre("Carlos García");

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getPacienteId());
        assertEquals("Ana Martínez", dto.getPacienteNombre());
        assertEquals("PENDIENTE", dto.getEstado());
        assertEquals("Revisión", dto.getMotivo());
        assertEquals("Cardiología", dto.getEspecialidad());
        assertEquals("Carlos García", dto.getMedicoNombre());
    }

    // ── DTO: CrearCitaDTO ────────────────────────────────────────

    @Test
    void crearCitaDTO_gettersYSetters_funcionanCorrectamente() {
        CrearCitaDTO dto = new CrearCitaDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(2L);
        dto.setFechaHora("2026-05-01T10:00");
        dto.setMotivo("Control");
        dto.setEspecialidad("Pediatría");

        assertEquals(1L, dto.getPacienteId());
        assertEquals(2L, dto.getMedicoId());
        assertEquals("Control", dto.getMotivo());
        assertEquals("Pediatría", dto.getEspecialidad());
    }

    @Test
    void crearCitaDTO_fechaHoraParsed_correcta() {
        CrearCitaDTO dto = new CrearCitaDTO();
        dto.setFechaHora("2026-05-01T10:30");
        LocalDateTime parsed = dto.getFechaHoraParsed();
        assertNotNull(parsed);
        assertEquals(2026, parsed.getYear());
        assertEquals(5, parsed.getMonthValue());
        assertEquals(1, parsed.getDayOfMonth());
        assertEquals(10, parsed.getHour());
        assertEquals(30, parsed.getMinute());
    }

    @Test
    void crearCitaDTO_fechaHoraNull_devuelveNull() {
        CrearCitaDTO dto = new CrearCitaDTO();
        dto.setFechaHora(null);
        assertNull(dto.getFechaHoraParsed());
    }

    // ── DTO: DiagnosticoDTO ──────────────────────────────────────

    @Test
    void diagnosticoDTO_gettersYSetters_funcionanCorrectamente() {
        DiagnosticoDTO dto = new DiagnosticoDTO();
        dto.setId(1L);
        dto.setPacienteId(2L);
        dto.setPacienteNombre("Pedro López");
        dto.setMedicoId(3L);
        dto.setMedicoNombre("Laura Sánchez");
        dto.setDescripcion("Gripe");
        dto.setTratamiento("Reposo");
        dto.setObservaciones("Beber agua");
        dto.setFechaDiagnostico(LocalDateTime.now());

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getPacienteId());
        assertEquals("Pedro López", dto.getPacienteNombre());
        assertEquals(3L, dto.getMedicoId());
        assertEquals("Laura Sánchez", dto.getMedicoNombre());
        assertEquals("Gripe", dto.getDescripcion());
        assertEquals("Reposo", dto.getTratamiento());
    }

    // ── DTO: HistorialMedicoDTO ──────────────────────────────────

    @Test
    void historialMedicoDTO_gettersYSetters_funcionanCorrectamente() {
        HistorialMedicoDTO dto = new HistorialMedicoDTO();
        dto.setNombreCompleto("Ana Martínez López");
        dto.setGrupoSanguineo("O+");
        dto.setAltura(1.70);
        dto.setPeso(65.0);
        dto.setAlergias("Penicilina");
        dto.setMedicacionActual("Paracetamol");
        dto.setEnfermedadesPrevias("Asma");
        dto.setAntecedentesFamiliares("Diabetes");
        dto.setObservacionesMedicas("Sin observaciones");

        assertEquals("Ana Martínez López", dto.getNombreCompleto());
        assertEquals("O+", dto.getGrupoSanguineo());
        assertEquals(1.70, dto.getAltura());
        assertEquals(65.0, dto.getPeso());
        assertEquals("Penicilina", dto.getAlergias());
        assertEquals("Paracetamol", dto.getMedicacionActual());
    }

    // ── DTO: LoginDTO ────────────────────────────────────────────

    @Test
    void loginDTO_gettersYSetters_funcionanCorrectamente() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("1234");

        assertEquals("test@test.com", dto.getEmail());
        assertEquals("1234", dto.getPassword());
    }

    // ── DTO: RegistroPacienteDTO ─────────────────────────────────

    @Test
    void registroPacienteDTO_gettersYSetters_funcionanCorrectamente() {
        RegistroPacienteDTO dto = new RegistroPacienteDTO();
        dto.setNombre("Luis");
        dto.setApellido1("García");
        dto.setApellido2("Pérez");
        dto.setDni("11111111C");
        dto.setEmail("luis@test.com");
        dto.setPassword("secret");
        dto.setTelefono("633333333");

        assertEquals("Luis", dto.getNombre());
        assertEquals("García", dto.getApellido1());
        assertEquals("11111111C", dto.getDni());
        assertEquals("luis@test.com", dto.getEmail());
    }

    // ── DTO: EditarPacienteDTO ───────────────────────────────────

    @Test
    void editarPacienteDTO_gettersYSetters_funcionanCorrectamente() {
        EditarPacienteDTO dto = new EditarPacienteDTO();
        dto.setNombre("Juan");
        dto.setApellido1("Sánchez");
        dto.setEmail("juan@test.com");
        dto.setTelefono("644444444");

        assertEquals("Juan", dto.getNombre());
        assertEquals("Sánchez", dto.getApellido1());
        assertEquals("juan@test.com", dto.getEmail());
    }

    // ── DTO: EditarMedicoDTO ─────────────────────────────────────

    @Test
    void editarMedicoDTO_gettersYSetters_funcionanCorrectamente() {
        EditarMedicoDTO dto = new EditarMedicoDTO();
        dto.setNombre("María");
        dto.setApellido1("Fernández");
        dto.setEmail("maria@hospital.com");
        dto.setEspecialidad("Neurología");

        assertEquals("María", dto.getNombre());
        assertEquals("Neurología", dto.getEspecialidad());
    }

    // ── DTO: EditarRecepcionistaDTO ──────────────────────────────

    @Test
    void editarRecepcionistaDTO_gettersYSetters_funcionanCorrectamente() {
        EditarRecepcionistaDTO dto = new EditarRecepcionistaDTO();
        dto.setNombre("Sara");
        dto.setApellido1("López");
        dto.setEmail("sara@hospital.com");

        assertEquals("Sara", dto.getNombre());
        assertEquals("López", dto.getApellido1());
        assertEquals("sara@hospital.com", dto.getEmail());
    }

    // ── ENTIDAD RECEPCIONISTA ────────────────────────────────────

    @Test
    void recepcionista_gettersYSetters_funcionanCorrectamente() {
        Recepcionista r = new Recepcionista();
        r.setId(1L);
        r.setNombre("María");
        r.setApellido1("López");
        r.setApellido2("García");
        r.setDni("99887766D");
        r.setEmail("maria@hospital.com");
        r.setPassword("pass123");
        r.setTelefono("655555555");
        r.setFechaNacimiento(LocalDate.of(1985, 3, 15));
        r.setSexo("F");
        r.setDireccion("Av. Central 10");
        r.setCiudad("Valencia");
        r.setProvincia("Valencia");
        r.setCodigoPostal("46001");
        r.setDepartamentoAsignado("Admisiones");
        r.setTurno("MAÑANA");
        r.setFechaIncorporacion(LocalDate.of(2020, 6, 1));
        r.setRol(Paciente.Rol.RECEPCIONISTA);
        r.setActivo(true);
        r.setFechaActualizacion(LocalDateTime.now());
        r.setCitas(new java.util.ArrayList<>());

        assertEquals(1L, r.getId());
        assertEquals("María", r.getNombre());
        assertEquals("López", r.getApellido1());
        assertEquals("García", r.getApellido2());
        assertEquals("99887766D", r.getDni());
        assertEquals("maria@hospital.com", r.getEmail());
        assertEquals("pass123", r.getPassword());
        assertEquals("655555555", r.getTelefono());
        assertEquals("F", r.getSexo());
        assertEquals("Admisiones", r.getDepartamentoAsignado());
        assertEquals("MAÑANA", r.getTurno());
        assertEquals(Paciente.Rol.RECEPCIONISTA, r.getRol());
        assertTrue(r.getActivo());
        assertNotNull(r.getCitas());
    }

    @Test
    void recepcionista_prePersist_inicializaValores() {
        Recepcionista r = new Recepcionista();
        r.prePersist();
        assertTrue(r.getActivo());
        assertEquals(Paciente.Rol.RECEPCIONISTA, r.getRol());
        assertNotNull(r.getFechaCreacion());
        assertNotNull(r.getFechaActualizacion());
    }

    @Test
    void recepcionista_preUpdate_actualizaFecha() {
        Recepcionista r = new Recepcionista();
        r.preUpdate();
        assertNotNull(r.getFechaActualizacion());
    }

    // ── DTO: CrearDiagnosticoDTO ─────────────────────────────────

    @Test
    void crearDiagnosticoDTO_gettersYSetters_funcionanCorrectamente() {
        com.gestionHospitalaria.dto.CrearDiagnosticoDTO dto = new com.gestionHospitalaria.dto.CrearDiagnosticoDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(2L);
        dto.setDescripcion("Bronquitis");
        dto.setTratamiento("Antibióticos");
        dto.setObservaciones("Reposo 3 días");

        assertEquals(1L, dto.getPacienteId());
        assertEquals(2L, dto.getMedicoId());
        assertEquals("Bronquitis", dto.getDescripcion());
        assertEquals("Antibióticos", dto.getTratamiento());
    }
}
