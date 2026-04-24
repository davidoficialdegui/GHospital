package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CrearDiagnosticoDTO;
import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.facade.DiagnosticoFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoController {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticoController.class);

    @Autowired
    private DiagnosticoFacade diagnosticoFacade;

    @PostMapping
    public DiagnosticoDTO registrar(@RequestBody CrearDiagnosticoDTO dto) {
        logger.info("POST /api/diagnosticos - pacienteId={}", dto.getPacienteId());
        return diagnosticoFacade.registrarDiagnostico(dto);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<DiagnosticoDTO> obtenerPorPaciente(@PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/diagnosticos/paciente/{}", pacienteId);
        return diagnosticoFacade.obtenerDiagnosticosPaciente(pacienteId);
    }
}