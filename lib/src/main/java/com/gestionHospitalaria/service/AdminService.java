package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.EditarMedicoDTO;
import com.gestionHospitalaria.dto.EditarPacienteDTO;
import com.gestionHospitalaria.dto.EditarRecepcionistaDTO;
import com.gestionHospitalaria.entity.Cita;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.entity.Recepcionista;
import com.gestionHospitalaria.repository.CitaRepository;
import com.gestionHospitalaria.repository.DiagnosticoRepository;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;
import com.gestionHospitalaria.repository.RecepcionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public List<Recepcionista> listarRecepcionistas() {
        return recepcionistaRepository.findAll();
    }

    public Paciente obtenerPaciente(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    public Medico obtenerMedico(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
    }

    public Recepcionista obtenerRecepcionista(Long id) {
        return recepcionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepcionista no encontrado"));
    }

    public void editarPaciente(Long id, EditarPacienteDTO dto) {
        Paciente paciente = obtenerPaciente(id);
        paciente.setNombre(dto.getNombre());
        paciente.setApellido1(dto.getApellido1());
        paciente.setApellido2(dto.getApellido2());
        paciente.setDni(dto.getDni());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefono(dto.getTelefono());
        paciente.setTelefonoEmergencia(dto.getTelefonoEmergencia());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setSexo(dto.getSexo());
        paciente.setDireccion(dto.getDireccion());
        paciente.setCiudad(dto.getCiudad());
        paciente.setProvincia(dto.getProvincia());
        paciente.setCodigoPostal(dto.getCodigoPostal());
        paciente.setGrupoSanguineo(dto.getGrupoSanguineo());
        paciente.setAltura(dto.getAltura());
        paciente.setPeso(dto.getPeso());
        paciente.setAlergias(dto.getAlergias());
        paciente.setEnfermedadesPrevias(dto.getEnfermedadesPrevias());
        paciente.setMedicacionActual(dto.getMedicacionActual());
        paciente.setAntecedentesFamiliares(dto.getAntecedentesFamiliares());
        paciente.setObservacionesMedicas(dto.getObservacionesMedicas());
        pacienteRepository.save(paciente);
    }

    public void editarMedico(Long id, EditarMedicoDTO dto) {
        Medico medico = obtenerMedico(id);
        medico.setNombre(dto.getNombre());
        medico.setApellido1(dto.getApellido1());
        medico.setApellido2(dto.getApellido2());
        medico.setDni(dto.getDni());
        medico.setEmail(dto.getEmail());
        medico.setTelefono(dto.getTelefono());
        medico.setFechaNacimiento(dto.getFechaNacimiento());
        medico.setSexo(dto.getSexo());
        medico.setDireccion(dto.getDireccion());
        medico.setCiudad(dto.getCiudad());
        medico.setProvincia(dto.getProvincia());
        medico.setCodigoPostal(dto.getCodigoPostal());
        medico.setEspecialidad(dto.getEspecialidad());
        medico.setSubespecialidad(dto.getSubespecialidad());
        medico.setDepartamento(dto.getDepartamento());
        medico.setAniosExperiencia(dto.getAniosExperiencia());
        medico.setTurno(dto.getTurno());
        medico.setDescripcionProfesional(dto.getDescripcionProfesional());
        medicoRepository.save(medico);
    }

    public Map<String, Long> obtenerEstadisticas() {
        Map<String, Long> stats = new HashMap<>();

        // Contar por rol real en las 3 tablas (un usuario guardado en recepcionistas
        // puede tener rol ADMIN, por eso no usamos count() a secas)
        long pacientes = pacienteRepository.countByRol(Paciente.Rol.PACIENTE)
                       + medicoRepository.countByRol(Paciente.Rol.PACIENTE)
                       + recepcionistaRepository.countByRol(Paciente.Rol.PACIENTE);

        long medicos = pacienteRepository.countByRol(Paciente.Rol.MEDICO)
                     + medicoRepository.countByRol(Paciente.Rol.MEDICO)
                     + recepcionistaRepository.countByRol(Paciente.Rol.MEDICO);

        long recepcionistas = pacienteRepository.countByRol(Paciente.Rol.RECEPCIONISTA)
                            + medicoRepository.countByRol(Paciente.Rol.RECEPCIONISTA)
                            + recepcionistaRepository.countByRol(Paciente.Rol.RECEPCIONISTA);

        stats.put("totalPacientes", pacientes);
        stats.put("totalMedicos", medicos);
        stats.put("totalRecepcionistas", recepcionistas);
        stats.put("totalCitas", citaRepository.count());
        stats.put("citasPendientes", citaRepository.countByEstado(Cita.EstadoCita.PENDIENTE));
        stats.put("citasConfirmadas", citaRepository.countByEstado(Cita.EstadoCita.CONFIRMADA));
        stats.put("citasCanceladas", citaRepository.countByEstado(Cita.EstadoCita.CANCELADA));
        stats.put("citasRealizadas", citaRepository.countByEstado(Cita.EstadoCita.REALIZADA));
        stats.put("totalDiagnosticos", diagnosticoRepository.count());
        return stats;
    }

    public void asignarRolPaciente(Long id, String rol) {
        Paciente paciente = obtenerPaciente(id);
        paciente.setRol(Paciente.Rol.valueOf(rol));
        pacienteRepository.save(paciente);
    }

    public void asignarRolMedico(Long id, String rol) {
        Medico medico = obtenerMedico(id);
        medico.setRol(Paciente.Rol.valueOf(rol));
        medicoRepository.save(medico);
    }

    public void asignarRolRecepcionista(Long id, String rol) {
        Recepcionista rec = obtenerRecepcionista(id);
        rec.setRol(Paciente.Rol.valueOf(rol));
        recepcionistaRepository.save(rec);
    }

    public void eliminarPaciente(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente no encontrado");
        }
        pacienteRepository.deleteById(id);
    }

    public void eliminarMedico(Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new RuntimeException("Médico no encontrado");
        }
        medicoRepository.deleteById(id);
    }

    public void eliminarRecepcionista(Long id) {
        if (!recepcionistaRepository.existsById(id)) {
            throw new RuntimeException("Recepcionista no encontrado");
        }
        recepcionistaRepository.deleteById(id);
    }

    public void editarRecepcionista(Long id, EditarRecepcionistaDTO dto) {
        Recepcionista rec = obtenerRecepcionista(id);
        rec.setNombre(dto.getNombre());
        rec.setApellido1(dto.getApellido1());
        rec.setApellido2(dto.getApellido2());
        rec.setDni(dto.getDni());
        rec.setEmail(dto.getEmail());
        rec.setTelefono(dto.getTelefono());
        rec.setFechaNacimiento(dto.getFechaNacimiento());
        rec.setSexo(dto.getSexo());
        rec.setDireccion(dto.getDireccion());
        rec.setCiudad(dto.getCiudad());
        rec.setProvincia(dto.getProvincia());
        rec.setCodigoPostal(dto.getCodigoPostal());
        rec.setDepartamentoAsignado(dto.getDepartamentoAsignado());
        rec.setTurno(dto.getTurno());
        recepcionistaRepository.save(rec);
    }
}
