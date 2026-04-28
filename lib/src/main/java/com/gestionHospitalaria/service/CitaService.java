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
    public List<CitaDTO> obtenerTodasLasCitas() {
        return citaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
        // Devuelve TODAS las citas del médico ordenadas por fecha
        List<Cita> citas = citaRepository.findByMedicoIdOrderByFechaHoraAsc(medicoId);
        return citas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CitaDTO cambiarEstado(Long citaId, String nuevoEstado) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + citaId));
        cita.setEstado(Cita.EstadoCita.valueOf(nuevoEstado));
        return mapToDTO(citaRepository.save(cita));
    }

    @Transactional
    public CitaDTO cancelarCita(Long citaId, Long pacienteId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + citaId));

        if (!cita.getPaciente().getId().equals(pacienteId)) {
            throw new RuntimeException("No tienes permiso para cancelar esta cita");
        }

        if (cita.getEstado() == Cita.EstadoCita.CANCELADA) {
            throw new RuntimeException("La cita ya está cancelada");
        }

        if (cita.getEstado() == Cita.EstadoCita.REALIZADA) {
            throw new RuntimeException("No se puede cancelar una cita ya realizada");
        }

        cita.setEstado(Cita.EstadoCita.CANCELADA);
        Cita saved = citaRepository.save(cita);
        return mapToDTO(saved);
    }

    private CitaDTO mapToDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setPacienteId(cita.getPaciente().getId());
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado() != null ? cita.getEstado().name() : "PENDIENTE");
        dto.setMotivo(cita.getMotivo());
        dto.setEspecialidad(cita.getEspecialidad());
        dto.setMedicoNombre(
                cita.getMedico().getNombre() + " " + cita.getMedico().getApellido1()
        );
        dto.setPacienteNombre(
                cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido1()
        );
        return dto;
    }
}