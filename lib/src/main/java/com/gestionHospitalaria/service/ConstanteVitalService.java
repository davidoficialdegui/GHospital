package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.ConstanteVitalDTO;
import com.gestionHospitalaria.dto.CrearConstanteVitalDTO;
import com.gestionHospitalaria.entity.ConstanteVital;
import com.gestionHospitalaria.entity.Enfermero;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.repository.ConstanteVitalRepository;
import com.gestionHospitalaria.repository.EnfermeroRepository;
import com.gestionHospitalaria.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Lógica de negocio para constantes vitales.
 *
 * Reglas:
 *   - Una toma debe tener al menos UN valor clínico medido.
 *   - Los valores deben caer dentro de rangos fisiológicamente plausibles.
 *     Si no, se rechaza (evitamos que un error de tecleo llegue al médico).
 *   - Paciente y enfermero deben existir.
 */
@Service
public class ConstanteVitalService {

    private static final Logger logger = LoggerFactory.getLogger(ConstanteVitalService.class);

    @Autowired private ConstanteVitalRepository constanteVitalRepository;
    @Autowired private PacienteRepository      pacienteRepository;
    @Autowired private EnfermeroRepository     enfermeroRepository;

    @Transactional
    public ConstanteVitalDTO registrar(CrearConstanteVitalDTO dto) {
        logger.info("Registrando constantes vitales pacienteId={} enfermeroId={}",
                dto.getPacienteId(), dto.getEnfermeroId());

        if (dto.getPacienteId() == null || dto.getEnfermeroId() == null) {
            throw new IllegalArgumentException("Se requiere pacienteId y enfermeroId");
        }

        if (!hayAlgunaConstante(dto)) {
            throw new IllegalArgumentException(
                "Debe registrarse al menos una constante vital (tensión, FC, FR, " +
                "temperatura, saturación O₂, glucemia o peso)");
        }

        validarRangos(dto);

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException(
                        "Paciente no encontrado con id: " + dto.getPacienteId()));

        Enfermero enfermero = enfermeroRepository.findById(dto.getEnfermeroId())
                .orElseThrow(() -> new RuntimeException(
                        "Enfermero no encontrado con id: " + dto.getEnfermeroId()));

        ConstanteVital cv = new ConstanteVital();
        cv.setPaciente(paciente);
        cv.setEnfermero(enfermero);
        cv.setTensionSistolica(dto.getTensionSistolica());
        cv.setTensionDiastolica(dto.getTensionDiastolica());
        cv.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        cv.setFrecuenciaRespiratoria(dto.getFrecuenciaRespiratoria());
        cv.setTemperatura(dto.getTemperatura());
        cv.setSaturacionOxigeno(dto.getSaturacionOxigeno());
        cv.setGlucemia(dto.getGlucemia());
        cv.setPeso(dto.getPeso());
        cv.setObservaciones(dto.getObservaciones());

        ConstanteVital saved = constanteVitalRepository.save(cv);
        logger.info("Constantes vitales registradas con id={}", saved.getId());
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ConstanteVitalDTO> obtenerPorPaciente(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new RuntimeException("Paciente no encontrado con id: " + pacienteId);
        }
        return constanteVitalRepository
                .findByPacienteIdOrderByFechaRegistroDesc(pacienteId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /** Devuelve la toma más reciente, o null si el paciente no tiene ninguna. */
    @Transactional(readOnly = true)
    public ConstanteVitalDTO obtenerUltimaPorPaciente(Long pacienteId) {
        List<ConstanteVital> tomas = constanteVitalRepository
                .findByPacienteIdOrderByFechaRegistroDesc(pacienteId);
        return tomas.isEmpty() ? null : mapToDTO(tomas.get(0));
    }

    // ---------------------------------------------------------------------
    //  Validaciones
    // ---------------------------------------------------------------------

    private boolean hayAlgunaConstante(CrearConstanteVitalDTO d) {
        return d.getTensionSistolica()       != null
            || d.getTensionDiastolica()      != null
            || d.getFrecuenciaCardiaca()     != null
            || d.getFrecuenciaRespiratoria() != null
            || d.getTemperatura()            != null
            || d.getSaturacionOxigeno()      != null
            || d.getGlucemia()               != null
            || d.getPeso()                   != null;
    }

    /**
     * Rechazamos valores claramente erróneos. Los rangos son generosos a propósito:
     * solo queremos descartar errores de tecleo (ej. "1200" mmHg de tensión), no
     * hacer diagnóstico clínico — esa es tarea del médico.
     */
    private void validarRangos(CrearConstanteVitalDTO d) {
        rango("Tensión sistólica",      d.getTensionSistolica(),      40, 300);
        rango("Tensión diastólica",     d.getTensionDiastolica(),     20, 200);
        rango("Frecuencia cardíaca",    d.getFrecuenciaCardiaca(),    20, 250);
        rango("Frecuencia respiratoria",d.getFrecuenciaRespiratoria(), 4,  80);
        rango("Saturación de oxígeno",  d.getSaturacionOxigeno(),     40, 100);
        rango("Glucemia",               d.getGlucemia(),              20, 800);

        if (d.getTemperatura() != null && (d.getTemperatura() < 28.0 || d.getTemperatura() > 45.0)) {
            throw new IllegalArgumentException(
                "Temperatura fuera de rango plausible (28–45 ºC): " + d.getTemperatura());
        }
        if (d.getPeso() != null && (d.getPeso() <= 0 || d.getPeso() > 400)) {
            throw new IllegalArgumentException(
                "Peso fuera de rango plausible (0–400 kg): " + d.getPeso());
        }
        // Coherencia tensión: sistólica >= diastólica
        if (d.getTensionSistolica() != null && d.getTensionDiastolica() != null
                && d.getTensionSistolica() < d.getTensionDiastolica()) {
            throw new IllegalArgumentException(
                "La tensión sistólica no puede ser menor que la diastólica");
        }
    }

    private void rango(String campo, Integer v, int min, int max) {
        if (v != null && (v < min || v > max)) {
            throw new IllegalArgumentException(
                campo + " fuera de rango plausible (" + min + "–" + max + "): " + v);
        }
    }

    // ---------------------------------------------------------------------
    //  Mapeo
    // ---------------------------------------------------------------------

    private ConstanteVitalDTO mapToDTO(ConstanteVital c) {
        ConstanteVitalDTO dto = new ConstanteVitalDTO();
        dto.setId(c.getId());
        dto.setPacienteId(c.getPaciente().getId());
        dto.setPacienteNombre(c.getPaciente().getNombre() + " " + c.getPaciente().getApellido1());
        dto.setEnfermeroId(c.getEnfermero().getId());
        dto.setEnfermeroNombre(c.getEnfermero().getNombre() + " " + c.getEnfermero().getApellido1());
        dto.setTensionSistolica(c.getTensionSistolica());
        dto.setTensionDiastolica(c.getTensionDiastolica());
        dto.setFrecuenciaCardiaca(c.getFrecuenciaCardiaca());
        dto.setFrecuenciaRespiratoria(c.getFrecuenciaRespiratoria());
        dto.setTemperatura(c.getTemperatura());
        dto.setSaturacionOxigeno(c.getSaturacionOxigeno());
        dto.setGlucemia(c.getGlucemia());
        dto.setPeso(c.getPeso());
        dto.setObservaciones(c.getObservaciones());
        dto.setFechaRegistro(c.getFechaRegistro());
        return dto;
    }
}