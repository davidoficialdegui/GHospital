package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.LoginDTO;
import com.gestionHospitalaria.dto.RegistroPacienteDTO;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

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

    public String login(LoginDTO dto) {
        Paciente paciente = pacienteRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), paciente.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return "Login correcto para: " + paciente.getNombre();
    }
}