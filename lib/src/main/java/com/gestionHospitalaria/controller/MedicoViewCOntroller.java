package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import com.gestionHospitalaria.facade.CitaFacade;
import com.gestionHospitalaria.facade.PacienteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/medico")
public class MedicoViewCOntroller {

    @Autowired
    private CitaFacade citaFacade;

    @Autowired
    private PacienteFacade pacienteFacade;

    /**
     * GET /medico/agenda?medicoId=1
     * Muestra la agenda de citas del día del médico.
     */
    @GetMapping("/agenda")
    public String verAgenda(
            @RequestParam(name = "medicoId", required = false) Long medicoId,
            Model model) {

        if (medicoId != null) {
            List<CitaDTO> citas = citaFacade.obtenerAgendaDelDia(medicoId);
            model.addAttribute("citas", citas);
            model.addAttribute("medicoId", medicoId);
        }

        return "agenda-medico";
    }

    /**
     * GET /medico/historial?pacienteId=1
     * Muestra el historial médico de un paciente.
     */
    @GetMapping("/historial")
    public String verHistorial(
            @RequestParam(name = "pacienteId", required = false) Long pacienteId,
            Model model) {

        if (pacienteId != null) {
            HistorialMedicoDTO historial = pacienteFacade.obtenerHistorial(pacienteId);
            model.addAttribute("historial", historial);
            model.addAttribute("pacienteId", pacienteId);
        }

        return "historial-paciente";
    }
}