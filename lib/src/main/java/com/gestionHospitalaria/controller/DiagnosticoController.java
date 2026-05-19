package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CrearDiagnosticoDTO;
import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.facade.DiagnosticoFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Diagnósticos", description = "Registro y consulta de diagnósticos médicos")
@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoController {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticoController.class);

    @Autowired
    private DiagnosticoFacade diagnosticoFacade;

    @Operation(summary = "Registrar un diagnóstico", description = "El médico registra un diagnóstico para un paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Diagnóstico registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Paciente o médico no encontrados, o descripción vacía")
    })
    @PostMapping
    public DiagnosticoDTO registrar(@RequestBody CrearDiagnosticoDTO dto) {
        logger.info("POST /api/diagnosticos - pacienteId={}", dto.getPacienteId());
        return diagnosticoFacade.registrarDiagnostico(dto);
    }

    @Operation(summary = "Diagnósticos de un paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de diagnósticos del paciente"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @GetMapping("/paciente/{pacienteId}")
    public List<DiagnosticoDTO> obtenerPorPaciente(
            @Parameter(description = "ID del paciente") @PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/diagnosticos/paciente/{}", pacienteId);
        return diagnosticoFacade.obtenerDiagnosticosPaciente(pacienteId);
    }
}