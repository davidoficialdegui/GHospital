package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.dto.CrearCitaDTO;
import com.gestionHospitalaria.facade.CitaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaFacade citaFacade;

    // Solicitar cita médica
    @PostMapping
    public CitaDTO crearCita(@RequestBody CrearCitaDTO dto) {
        return citaFacade.crearCita(dto);
    }

    // Ver citas del paciente
    @GetMapping("/paciente/{id}")
    public List<CitaDTO> obtenerCitas(@PathVariable Long id) {
        return citaFacade.obtenerCitasPaciente(id);
    }
}