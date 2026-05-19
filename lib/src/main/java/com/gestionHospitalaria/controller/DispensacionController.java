package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CrearDispensacionDTO;
import com.gestionHospitalaria.dto.DispensacionDTO;
import com.gestionHospitalaria.facade.DispensacionFacade;
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

@Tag(name = "Dispensaciones", description = "Gestión de entregas de medicamentos por el farmacéutico")
@RestController
@RequestMapping("/api/dispensaciones")
public class DispensacionController {

    private static final Logger logger = LoggerFactory.getLogger(DispensacionController.class);

    @Autowired
    private DispensacionFacade dispensacionFacade;

    @Operation(summary = "Registrar una dispensación", description = "El farmacéutico registra la entrega de medicamentos de una receta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dispensación registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos (cantidad <= 0, receta o farmacéutico no encontrados)")
    })
    @PostMapping
    public DispensacionDTO registrar(@RequestBody CrearDispensacionDTO dto) {
        logger.info("POST /api/dispensaciones - recetaId={}, farmaceuticoId={}", dto.getRecetaId(), dto.getFarmaceuticoId());
        return dispensacionFacade.registrarDispensacion(dto);
    }

    @Operation(summary = "Dispensaciones de un paciente", description = "Devuelve todas las dispensaciones realizadas a un paciente, ordenadas por fecha")
    @ApiResponse(responseCode = "200", description = "Lista de dispensaciones (puede estar vacía)")
    @GetMapping("/paciente/{pacienteId}")
    public List<DispensacionDTO> obtenerPorPaciente(
            @Parameter(description = "ID del paciente") @PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/dispensaciones/paciente/{}", pacienteId);
        return dispensacionFacade.obtenerPorPaciente(pacienteId);
    }

    @Operation(summary = "Dispensaciones realizadas por un farmacéutico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de dispensaciones"),
        @ApiResponse(responseCode = "404", description = "Farmacéutico no encontrado")
    })
    @GetMapping("/farmaceutico/{farmaceuticoId}")
    public List<DispensacionDTO> obtenerPorFarmaceutico(
            @Parameter(description = "ID del farmacéutico") @PathVariable("farmaceuticoId") Long farmaceuticoId) {
        logger.info("GET /api/dispensaciones/farmaceutico/{}", farmaceuticoId);
        return dispensacionFacade.obtenerPorFarmaceutico(farmaceuticoId);
    }

    @Operation(summary = "Dispensaciones de una receta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de dispensaciones"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    })
    @GetMapping("/receta/{recetaId}")
    public List<DispensacionDTO> obtenerPorReceta(
            @Parameter(description = "ID de la receta") @PathVariable("recetaId") Long recetaId) {
        logger.info("GET /api/dispensaciones/receta/{}", recetaId);
        return dispensacionFacade.obtenerPorReceta(recetaId);
    }
}
