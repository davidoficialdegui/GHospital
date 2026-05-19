package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CrearDispensacionDTO;
import com.gestionHospitalaria.dto.DispensacionDTO;
import com.gestionHospitalaria.facade.DispensacionFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispensaciones")
public class DispensacionController {

    private static final Logger logger = LoggerFactory.getLogger(DispensacionController.class);

    @Autowired
    private DispensacionFacade dispensacionFacade;

    @PostMapping
    public DispensacionDTO registrar(@RequestBody CrearDispensacionDTO dto) {
        logger.info("POST /api/dispensaciones - recetaId={}, farmaceuticoId={}", dto.getRecetaId(), dto.getFarmaceuticoId());
        return dispensacionFacade.registrarDispensacion(dto);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<DispensacionDTO> obtenerPorPaciente(@PathVariable("pacienteId") Long pacienteId) {
        logger.info("GET /api/dispensaciones/paciente/{}", pacienteId);
        return dispensacionFacade.obtenerPorPaciente(pacienteId);
    }

    @GetMapping("/farmaceutico/{farmaceuticoId}")
    public List<DispensacionDTO> obtenerPorFarmaceutico(@PathVariable("farmaceuticoId") Long farmaceuticoId) {
        logger.info("GET /api/dispensaciones/farmaceutico/{}", farmaceuticoId);
        return dispensacionFacade.obtenerPorFarmaceutico(farmaceuticoId);
    }

    @GetMapping("/receta/{recetaId}")
    public List<DispensacionDTO> obtenerPorReceta(@PathVariable("recetaId") Long recetaId) {
        logger.info("GET /api/dispensaciones/receta/{}", recetaId);
        return dispensacionFacade.obtenerPorReceta(recetaId);
    }
}
