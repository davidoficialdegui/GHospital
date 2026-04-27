package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import com.gestionHospitalaria.dto.LoginDTO;
import com.gestionHospitalaria.dto.RegistroPacienteDTO;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.entity.Recepcionista;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;
import com.gestionHospitalaria.repository.RecepcionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private RecepcionistaRepository recepcionistaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Paciente registrar(RegistroPacienteDTO dto) {
        if (pacienteRepository.existsByDni(dto.getDni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }
        if (pacienteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Paciente paciente = new Paciente();
        paciente.setNombre(dto.getNombre());
        paciente.setApellido1(dto.getApellido1());
        paciente.setApellido2(dto.getApellido2());
        paciente.setDni(dto.getDni());
        paciente.setEmail(dto.getEmail());
        paciente.setPassword(passwordEncoder.encode(dto.getPassword()));
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setTelefono(dto.getTelefono());
        paciente.setActivo(true);
        paciente.setRol(Paciente.Rol.PACIENTE);

        return pacienteRepository.save(paciente);
    }

    // Devuelve "ROL|ID|NOMBRE" para que el controlador redirija correctamente
    public String login(LoginDTO dto) {
        // Buscar en pacientes
        java.util.Optional<Paciente> pacienteOpt = pacienteRepository.findByEmail(dto.getEmail());
        if (pacienteOpt.isPresent()) {
            Paciente p = pacienteOpt.get();
            if (!passwordEncoder.matches(dto.getPassword(), p.getPassword())) {
                throw new RuntimeException("Contraseña incorrecta");
            }
            return p.getRol().name() + "|" + p.getId() + "|" + p.getNombre();
        }

        // Buscar en médicos
        java.util.Optional<Medico> medicoOpt = medicoRepository.findByEmail(dto.getEmail());
        if (medicoOpt.isPresent()) {
            Medico m = medicoOpt.get();
            if (!passwordEncoder.matches(dto.getPassword(), m.getPassword())) {
                throw new RuntimeException("Contraseña incorrecta");
            }
            return m.getRol().name() + "|" + m.getId() + "|" + m.getNombre();
        }

        // Buscar en recepcionistas (también incluye admins)
        java.util.Optional<Recepcionista> recOpt = recepcionistaRepository.findByEmail(dto.getEmail());
        if (recOpt.isPresent()) {
            Recepcionista r = recOpt.get();
            if (!passwordEncoder.matches(dto.getPassword(), r.getPassword())) {
                throw new RuntimeException("Contraseña incorrecta");
            }
            return r.getRol().name() + "|" + r.getId() + "|" + r.getNombre();
        }

        throw new RuntimeException("Usuario no encontrado");
    }

    public HistorialMedicoDTO obtenerHistorial(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        HistorialMedicoDTO dto = new HistorialMedicoDTO();
        dto.setNombreCompleto(
                paciente.getNombre() + " " +
                paciente.getApellido1() + " " +
                paciente.getApellido2()
        );
        dto.setGrupoSanguineo(paciente.getGrupoSanguineo());
        dto.setAltura(paciente.getAltura());
        dto.setPeso(paciente.getPeso());
        dto.setAlergias(paciente.getAlergias());
        dto.setEnfermedadesPrevias(paciente.getEnfermedadesPrevias());
        dto.setMedicacionActual(paciente.getMedicacionActual());
        dto.setAntecedentesFamiliares(paciente.getAntecedentesFamiliares());
        dto.setObservacionesMedicas(paciente.getObservacionesMedicas());

        return dto;
    }
}