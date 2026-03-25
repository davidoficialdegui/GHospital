package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.dto.CrearCitaDTO;
import com.gestionHospitalaria.entity.Cita;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.repository.CitaRepository;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;     

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;


    //Solicitar cita médica
    public CitaDTO crearCita(CrearCitaDTO dto) {

        
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        
        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());
        cita.setEspecialidad(dto.getEspecialidad());

       
        Cita saved = citaRepository.save(cita);

        return mapToDTO(saved);
    }

    //Ver citas del paciente
    public List<CitaDTO> obtenerCitasPaciente(Long pacienteId) {

        // 1. Obtener citas del repositorio
        List<Cita> citas = citaRepository.findByPacienteId(pacienteId);

        // 2. Convertir a DTO
        return citas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    
    private CitaDTO mapToDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado().name());
        dto.setMotivo(cita.getMotivo());
        dto.setEspecialidad(cita.getEspecialidad());
        dto.setMedicoNombre(
                cita.getMedico().getNombre() + " " + cita.getMedico().getApellido1()
        );
        return dto;
    }
}