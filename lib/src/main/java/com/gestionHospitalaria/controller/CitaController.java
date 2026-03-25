package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.dto.CrearCitaDTO;
import com.gestionHospitalaria.facade.CitaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaFacade citaFacade;

    @PostMapping
    @ResponseBody
    public CitaDTO crearCita(@RequestBody CrearCitaDTO dto) {
        return citaFacade.crearCita(dto);
    }

    @GetMapping("/paciente/{id}")
    @ResponseBody
    public List<CitaDTO> obtenerCitas(@PathVariable("id") Long id) {
        return citaFacade.obtenerCitasPaciente(id);
    }

    @GetMapping("/medico/{medicoId}/hoy")
    @ResponseBody
    public List<CitaDTO> agendaHoy(@PathVariable("medicoId") Long medicoId) {
        return citaFacade.obtenerAgendaDelDia(medicoId);
    }
}