package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CrearRecetaDTO;
import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.entity.Receta;
import com.gestionHospitalaria.repository.MedicoRepository;
import com.gestionHospitalaria.repository.PacienteRepository;
import com.gestionHospitalaria.repository.RecetaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecetaService {

    private static final Logger logger = LoggerFactory.getLogger(RecetaService.class);

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional
    public RecetaDTO crearReceta(CrearRecetaDTO dto) {
        logger.info("Creando receta para pacienteId={} por medicoId={}", dto.getPacienteId(), dto.getMedicoId());

        if (dto.getMedicamento() == null || dto.getMedicamento().isBlank()) {
            throw new IllegalArgumentException("El nombre del medicamento no puede estar vacío");
        }
        if (dto.getDosis() == null || dto.getDosis().isBlank()) {
            throw new IllegalArgumentException("La dosis no puede estar vacía");
        }
        if (dto.getPosologia() == null || dto.getPosologia().isBlank()) {
            throw new IllegalArgumentException("La posología no puede estar vacía");
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

        Receta receta = new Receta();
        receta.setPaciente(paciente);
        receta.setMedico(medico);
        receta.setMedicamento(dto.getMedicamento());
        receta.setDosis(dto.getDosis());
        receta.setPosologia(dto.getPosologia());
        receta.setDuracionDias(dto.getDuracionDias());
        receta.setInstrucciones(dto.getInstrucciones());
        receta.setFechaVencimiento(dto.getFechaVencimiento());

        Receta saved = recetaRepository.save(receta);
        logger.info("Receta creada correctamente con id={}", saved.getId());

        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<RecetaDTO> obtenerRecetasPaciente(Long pacienteId) {
        logger.info("Obteniendo recetas del pacienteId={}", pacienteId);

        if (!pacienteRepository.existsById(pacienteId)) {
            throw new RuntimeException("Paciente no encontrado con id: " + pacienteId);
        }

        return recetaRepository.findByPacienteIdOrderByFechaEmisionDesc(pacienteId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecetaDTO obtenerRecetaPorId(Long id) {
        logger.info("Obteniendo receta con id={}", id);
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + id));
        return mapToDTO(receta);
    }

    private RecetaDTO mapToDTO(Receta r) {
        RecetaDTO dto = new RecetaDTO();
        dto.setId(r.getId());
        dto.setPacienteId(r.getPaciente().getId());
        dto.setPacienteNombre(r.getPaciente().getNombre() + " " + r.getPaciente().getApellido1());
        dto.setMedicoId(r.getMedico().getId());
        dto.setMedicoNombre(r.getMedico().getNombre() + " " + r.getMedico().getApellido1());
        dto.setMedicamento(r.getMedicamento());
        dto.setDosis(r.getDosis());
        dto.setPosologia(r.getPosologia());
        dto.setDuracionDias(r.getDuracionDias());
        dto.setInstrucciones(r.getInstrucciones());
        dto.setFechaEmision(r.getFechaEmision());
        dto.setFechaVencimiento(r.getFechaVencimiento());
        dto.setFechaCreacion(r.getFechaCreacion());
        return dto;
    }
}