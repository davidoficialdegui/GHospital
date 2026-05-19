package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CrearRecetaDTO;
import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.facade.RecetaFacade;
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

@Tag(name = "Recetas", description = "Gestión de recetas médicas")
@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private static final Logger logger = LoggerFactory.getLogger(RecetaController.class);

    @Autowired
    private RecetaFacade recetaFacade;

    @Operation(summary = "Crear una receta", description = "El médico emite una nueva receta para un paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Receta creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos (medicamento, dosis o posología vacíos, paciente o médico no encontrados)")
    })
    @PostMapping
    public RecetaDTO crear(@RequestBody CrearRecetaDTO dto) {
        logger.info("POST /api/recetas - pacienteId={}, medicoId={}", dto.getPacienteId(), dto.getMedicoId());
        return recetaFacade.crearReceta(dto);
    }

    @Operation(summary = "Listar todas las recetas")
    @ApiResponse(responseCode = "200", description = "Lista completa de recetas")
    @GetMapping
    public List<RecetaDTO> obtenerTodas() {
        logger.info("GET /api/recetas");
        return recetaFacade.obtenerTodasLasRecetas();
    }

    @Operation(summary = "Obtener receta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Receta encontrada"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    })
    @GetMapping("/{id}")
    public RecetaDTO obtenerPorId(
            @Parameter(description = "ID de la receta") @PathVariable("id") Long id) {
        logger.info("GET /api/recetas/{}", id);
        return recetaFacade.obtenerRecetaPorId(id);
    }

    @Operation(summary = "Recetas de un paciente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de recetas del paciente"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @GetMapping("/paciente/{pacienteId}")
    public List<RecetaDTO> obtenerPorPaciente(
            @Parameter(description = "ID del paciente") @PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/recetas/paciente/{}", pacienteId);
        return recetaFacade.obtenerRecetasPaciente(pacienteId);
    }

    @Operation(summary = "Recetas de un paciente por DNI", description = "Usado por el farmacéutico para buscar recetas a partir del DNI del paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de recetas (puede estar vacía)"),
        @ApiResponse(responseCode = "404", description = "Paciente con ese DNI no encontrado")
    })
    @GetMapping("/paciente/dni/{dni}")
    public List<RecetaDTO> obtenerPorDniPaciente(
            @Parameter(description = "DNI del paciente (ej: 12345678A)") @PathVariable("dni") String dni) {
        logger.info("GET /api/recetas/paciente/dni/{}", dni);
        return recetaFacade.obtenerRecetasPorDniPaciente(dni);
    }
}
