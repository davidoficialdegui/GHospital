package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.ConstanteVitalDTO;
import com.gestionHospitalaria.dto.CrearConstanteVitalDTO;
import com.gestionHospitalaria.facade.ConstanteVitalFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST de constantes vitales.
 * Para uso programático (tests, integraciones futuras).
 * El front del enfermero usa EnfermeroViewController.
 */
@RestController
@RequestMapping("/api/constantes")
public class ConstanteVitalController {

    private static final Logger logger = LoggerFactory.getLogger(ConstanteVitalController.class);

    @Autowired
    private ConstanteVitalFacade facade;

    @PostMapping
    public ConstanteVitalDTO registrar(@RequestBody CrearConstanteVitalDTO dto) {
        logger.info("POST /api/constantes pacienteId={}", dto.getPacienteId());
        return facade.registrar(dto);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<ConstanteVitalDTO> obtenerPorPaciente(@PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/constantes/paciente/{}", pacienteId);
        return facade.obtenerPorPaciente(pacienteId);
    }

    @GetMapping("/paciente/{pacienteId}/ultima")
    public ConstanteVitalDTO ultima(@PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/constantes/paciente/{}/ultima", pacienteId);
        return facade.obtenerUltimaPorPaciente(pacienteId);
    }
}