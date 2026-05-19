package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.CrearDispensacionDTO;
import com.gestionHospitalaria.dto.DispensacionDTO;
import com.gestionHospitalaria.entity.Dispensacion;
import com.gestionHospitalaria.entity.Farmaceutico;
import com.gestionHospitalaria.entity.Receta;
import com.gestionHospitalaria.repository.DispensacionRepository;
import com.gestionHospitalaria.repository.FarmaceuticoRepository;
import com.gestionHospitalaria.repository.RecetaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispensacionService {

    private static final Logger logger = LoggerFactory.getLogger(DispensacionService.class);

    @Autowired
    private DispensacionRepository dispensacionRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private FarmaceuticoRepository farmaceuticoRepository;

    @Transactional
    public DispensacionDTO registrarDispensacion(CrearDispensacionDTO dto) {
        logger.info("Registrando dispensación para recetaId={} por farmaceuticoId={}", dto.getRecetaId(), dto.getFarmaceuticoId());

        if (dto.getCantidadDispensada() == null || dto.getCantidadDispensada() <= 0) {
            logger.warn("Cantidad dispensada inválida: {}", dto.getCantidadDispensada());
            throw new IllegalArgumentException("La cantidad dispensada debe ser mayor que cero");
        }

        Receta receta = recetaRepository.findById(dto.getRecetaId())
                .orElseThrow(() -> {
                    logger.error("Receta no encontrada con id={}", dto.getRecetaId());
                    return new RuntimeException("Receta no encontrada con id: " + dto.getRecetaId());
                });

        Farmaceutico farmaceutico = farmaceuticoRepository.findById(dto.getFarmaceuticoId())
                .orElseThrow(() -> {
                    logger.error("Farmacéutico no encontrado con id={}", dto.getFarmaceuticoId());
                    return new RuntimeException("Farmacéutico no encontrado con id: " + dto.getFarmaceuticoId());
                });

        Dispensacion dispensacion = new Dispensacion();
        dispensacion.setReceta(receta);
        dispensacion.setFarmaceutico(farmaceutico);
        dispensacion.setCantidadDispensada(dto.getCantidadDispensada());
        dispensacion.setObservaciones(dto.getObservaciones());

        if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
            dispensacion.setEstado(Dispensacion.EstadoDispensacion.valueOf(dto.getEstado().toUpperCase()));
        }

        Dispensacion saved = dispensacionRepository.save(dispensacion);
        logger.info("Dispensación registrada con id={}", saved.getId());
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<DispensacionDTO> obtenerPorPaciente(Long pacienteId) {
        logger.info("Obteniendo dispensaciones del pacienteId={}", pacienteId);
        List<Dispensacion> lista = dispensacionRepository
                .findByRecetaPacienteIdOrderByFechaDispensacionDesc(pacienteId);
        logger.info("Se encontraron {} dispensaciones para pacienteId={}", lista.size(), pacienteId);
        return lista.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DispensacionDTO> obtenerPorFarmaceutico(Long farmaceuticoId) {
        logger.info("Obteniendo dispensaciones del farmaceuticoId={}", farmaceuticoId);
        if (!farmaceuticoRepository.existsById(farmaceuticoId)) {
            logger.error("Farmacéutico no encontrado con id={}", farmaceuticoId);
            throw new RuntimeException("Farmacéutico no encontrado con id: " + farmaceuticoId);
        }
        List<Dispensacion> lista = dispensacionRepository
                .findByFarmaceuticoIdOrderByFechaDispensacionDesc(farmaceuticoId);
        logger.info("Se encontraron {} dispensaciones para farmaceuticoId={}", lista.size(), farmaceuticoId);
        return lista.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DispensacionDTO> obtenerPorReceta(Long recetaId) {
        logger.info("Obteniendo dispensaciones de recetaId={}", recetaId);
        if (!recetaRepository.existsById(recetaId)) {
            logger.error("Receta no encontrada con id={}", recetaId);
            throw new RuntimeException("Receta no encontrada con id: " + recetaId);
        }
        List<Dispensacion> lista = dispensacionRepository
                .findByRecetaIdOrderByFechaDispensacionDesc(recetaId);
        return lista.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private DispensacionDTO mapToDTO(Dispensacion d) {
        DispensacionDTO dto = new DispensacionDTO();
        dto.setId(d.getId());
        dto.setRecetaId(d.getReceta().getId());
        dto.setMedicamento(d.getReceta().getMedicamento());
        dto.setDosis(d.getReceta().getDosis());
        dto.setPacienteId(d.getReceta().getPaciente().getId());
        dto.setPacienteNombre(d.getReceta().getPaciente().getNombre() + " " + d.getReceta().getPaciente().getApellido1());
        dto.setFarmaceuticoId(d.getFarmaceutico().getId());
        dto.setFarmaceuticoNombre(d.getFarmaceutico().getNombre() + " " + d.getFarmaceutico().getApellido1());
        dto.setCantidadDispensada(d.getCantidadDispensada());
        dto.setEstado(d.getEstado().name());
        dto.setObservaciones(d.getObservaciones());
        dto.setFechaDispensacion(d.getFechaDispensacion());
        dto.setFechaCreacion(d.getFechaCreacion());
        return dto;
    }
}
