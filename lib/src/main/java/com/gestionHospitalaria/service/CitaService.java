package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.dto.CrearCitaDTO;
import com.gestionHospitalaria.entity.Cita;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.repository.CitaRepository;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public CitaDTO crearCita(CrearCitaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException(
                        "Paciente no encontrado con ID: " + dto.getPacienteId()));

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException(
                        "Médico no encontrado con ID: " + dto.getMedicoId()));

        LocalDateTime fechaHora = dto.getFechaHoraParsed();
        if (fechaHora == null) {
            throw new RuntimeException("La fecha y hora no puede estar vacía");
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaHora(fechaHora);
        cita.setMotivo(dto.getMotivo());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setEstado(Cita.EstadoCita.PENDIENTE);

        Cita saved = citaRepository.save(cita);
        return mapToDTO(saved);
    }

    @Transactional
    public List<CitaDTO> obtenerCitasPaciente(Long pacienteId) { 
        List<Cita> citas = citaRepository.findByPacienteId(pacienteId);
        return citas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CitaDTO> obtenerAgendaDelDia(Long medicoId) {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23, 59, 59);
        List<Cita> citas = citaRepository
                .findByMedicoIdAndFechaHoraBetween(medicoId, inicio, fin);
        return citas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CitaDTO mapToDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado() != null ? cita.getEstado().name() : "PENDIENTE");
        dto.setMotivo(cita.getMotivo());
        dto.setEspecialidad(cita.getEspecialidad());
        dto.setMedicoNombre(
                cita.getMedico().getNombre() + " " + cita.getMedico().getApellido1()
        );
        return dto;
    }
}