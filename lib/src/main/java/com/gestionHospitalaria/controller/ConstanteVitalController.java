package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.ConstanteVitalDTO;
import com.gestionHospitalaria.dto.CrearConstanteVitalDTO;
import com.gestionHospitalaria.facade.ConstanteVitalFacade;
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

@Tag(name = "Constantes Vitales", description = "Registro y consulta de constantes vitales de pacientes")
@RestController
@RequestMapping("/api/constantes")
public class ConstanteVitalController {

    private static final Logger logger = LoggerFactory.getLogger(ConstanteVitalController.class);

    @Autowired
    private ConstanteVitalFacade facade;

    @Operation(summary = "Registrar una toma de constantes", description = "El enfermero registra tensión, pulso, temperatura y saturación de un paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Constantes registradas correctamente"),
        @ApiResponse(responseCode = "400", description = "Paciente o enfermero no encontrados")
    })
    @PostMapping
    public ConstanteVitalDTO registrar(@RequestBody CrearConstanteVitalDTO dto) {
        logger.info("POST /api/constantes pacienteId={}", dto.getPacienteId());
        return facade.registrar(dto);
    }

    @Operation(summary = "Historial de constantes de un paciente")
    @ApiResponse(responseCode = "200", description = "Lista de tomas ordenadas por fecha descendente")
    @GetMapping("/paciente/{pacienteId}")
    public List<ConstanteVitalDTO> obtenerPorPaciente(
            @Parameter(description = "ID del paciente") @PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/constantes/paciente/{}", pacienteId);
        return facade.obtenerPorPaciente(pacienteId);
    }

    @Operation(summary = "Última toma de constantes de un paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Última toma registrada"),
        @ApiResponse(responseCode = "404", description = "No hay tomas para ese paciente")
    })
    @GetMapping("/paciente/{pacienteId}/ultima")
    public ConstanteVitalDTO ultima(
            @Parameter(description = "ID del paciente") @PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/constantes/paciente/{}/ultima", pacienteId);
        return facade.obtenerUltimaPorPaciente(pacienteId);
    }
}