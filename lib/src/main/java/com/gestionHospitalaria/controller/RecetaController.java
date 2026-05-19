package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CrearRecetaDTO;
import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.facade.RecetaFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private static final Logger logger = LoggerFactory.getLogger(RecetaController.class);

    @Autowired
    private RecetaFacade recetaFacade;

    @PostMapping
    public RecetaDTO crear(@RequestBody CrearRecetaDTO dto) {
        logger.info("POST /api/recetas - pacienteId={}, medicoId={}", dto.getPacienteId(), dto.getMedicoId());
        return recetaFacade.crearReceta(dto);
    }

    @GetMapping
    public List<RecetaDTO> obtenerTodas() {
        logger.info("GET /api/recetas");
        return recetaFacade.obtenerTodasLasRecetas();
    }

    @GetMapping("/{id}")
    public RecetaDTO obtenerPorId(@PathVariable("id") Long id) {
        logger.info("GET /api/recetas/{}", id);
        return recetaFacade.obtenerRecetaPorId(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<RecetaDTO> obtenerPorPaciente(@PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/recetas/paciente/{}", pacienteId);
        return recetaFacade.obtenerRecetasPaciente(pacienteId);
    }

    @GetMapping("/paciente/dni/{dni}")
    public List<RecetaDTO> obtenerPorDniPaciente(@PathVariable("dni") String dni) {
        logger.info("GET /api/recetas/paciente/dni/{}", dni);
        return recetaFacade.obtenerRecetasPorDniPaciente(dni);
    }
}
