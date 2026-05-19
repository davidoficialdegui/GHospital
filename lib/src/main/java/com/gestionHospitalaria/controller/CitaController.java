package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.dto.CrearCitaDTO;
import com.gestionHospitalaria.facade.CitaFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Citas", description = "Gestión de citas médicas")
@Controller
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaFacade citaFacade;

    @Operation(summary = "Crear una cita", description = "La recepcionista crea una nueva cita para un paciente con un médico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Paciente o médico no encontrados, o fecha inválida")
    })
    @PostMapping
    @ResponseBody
    public CitaDTO crearCita(@RequestBody CrearCitaDTO dto) {
        return citaFacade.crearCita(dto);
    }

    @Operation(summary = "Citas de un paciente")
    @ApiResponse(responseCode = "200", description = "Lista de citas del paciente")
    @GetMapping("/paciente/{id}")
    @ResponseBody
    public List<CitaDTO> obtenerCitas(
            @Parameter(description = "ID del paciente") @PathVariable("id") Long id) {
        return citaFacade.obtenerCitasPaciente(id);
    }

    @Operation(summary = "Agenda del médico para hoy", description = "Devuelve las citas del médico para el día actual")
    @ApiResponse(responseCode = "200", description = "Lista de citas de hoy")
    @GetMapping("/medico/{medicoId}/hoy")
    @ResponseBody
    public List<CitaDTO> agendaHoy(
            @Parameter(description = "ID del médico") @PathVariable("medicoId") Long medicoId) {
        return citaFacade.obtenerAgendaDelDia(medicoId);
    }

    @Operation(summary = "Cancelar una cita", description = "El paciente cancela una de sus citas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita cancelada"),
        @ApiResponse(responseCode = "400", description = "La cita no pertenece al paciente o ya está cancelada")
    })
    @PatchMapping("/{citaId}/cancelar")
    @ResponseBody
    public CitaDTO cancelarCita(
            @Parameter(description = "ID de la cita") @PathVariable Long citaId,
            @Parameter(description = "ID del paciente que solicita la cancelación") @RequestParam Long pacienteId) {
        return citaFacade.cancelarCita(citaId, pacienteId);
    }
}