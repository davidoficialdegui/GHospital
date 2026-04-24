package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CrearDiagnosticoDTO;
import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.entity.Diagnostico;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.repository.DiagnosticoRepository;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiagnosticoService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticoService.class);

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional
    public DiagnosticoDTO registrarDiagnostico(CrearDiagnosticoDTO dto) {
        logger.info("Registrando diagnóstico para pacienteId={} por medicoId={}", dto.getPacienteId(), dto.getMedicoId());

        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            logger.warn("Intento de registrar diagnóstico sin descripción");
            throw new IllegalArgumentException("La descripción del diagnóstico no puede estar vacía");
        }

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> {
                    logger.error("Paciente no encontrado con id={}", dto.getPacienteId());
                    return new RuntimeException("Paciente no encontrado con id: " + dto.getPacienteId());
                });

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> {
                    logger.error("Médico no encontrado con id={}", dto.getMedicoId());
                    return new RuntimeException("Médico no encontrado con id: " + dto.getMedicoId());
                });

        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setPaciente(paciente);
        diagnostico.setMedico(medico);
        diagnostico.setDescripcion(dto.getDescripcion());
        diagnostico.setTratamiento(dto.getTratamiento());
        diagnostico.setObservaciones(dto.getObservaciones());

        Diagnostico saved = diagnosticoRepository.save(diagnostico);
        logger.info("Diagnóstico registrado correctamente con id={}", saved.getId());

        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<DiagnosticoDTO> obtenerDiagnosticosPaciente(Long pacienteId) {
        logger.info("Obteniendo diagnósticos del pacienteId={}", pacienteId);

        if (!pacienteRepository.existsById(pacienteId)) {
            logger.error("Paciente no encontrado con id={}", pacienteId);
            throw new RuntimeException("Paciente no encontrado con id: " + pacienteId);
        }

        List<Diagnostico> diagnosticos = diagnosticoRepository
                .findByPacienteIdOrderByFechaDiagnosticoDesc(pacienteId);

        logger.info("Se encontraron {} diagnósticos para pacienteId={}", diagnosticos.size(), pacienteId);
        return diagnosticos.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private DiagnosticoDTO mapToDTO(Diagnostico d) {
        DiagnosticoDTO dto = new DiagnosticoDTO();
        dto.setId(d.getId());
        dto.setPacienteId(d.getPaciente().getId());
        dto.setPacienteNombre(d.getPaciente().getNombre() + " " + d.getPaciente().getApellido1());
        dto.setMedicoId(d.getMedico().getId());
        dto.setMedicoNombre(d.getMedico().getNombre() + " " + d.getMedico().getApellido1());
        dto.setDescripcion(d.getDescripcion());
        dto.setTratamiento(d.getTratamiento());
        dto.setObservaciones(d.getObservaciones());
        dto.setFechaDiagnostico(d.getFechaDiagnostico());
        return dto;
    }
}