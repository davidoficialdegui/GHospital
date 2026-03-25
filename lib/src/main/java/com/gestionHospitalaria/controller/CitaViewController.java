package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.dto.CrearCitaDTO;
import com.gestionHospitalaria.facade.CitaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/citas")
public class CitaViewController {

    @Autowired
    private CitaFacade citaFacade;

    @GetMapping
    public String mostrarCitas() {
        return "citas";
    }

    @GetMapping("/crear")
    public String mostrarFormulario() {
        return "crear-cita";
    }

    @PostMapping("/crear")
    @ResponseBody
    public String crearCita(@ModelAttribute CrearCitaDTO dto) {
        try {
            String info = "DATOS RECIBIDOS:<br>" +
                    "pacienteId = " + dto.getPacienteId() + "<br>" +
                    "medicoId = " + dto.getMedicoId() + "<br>" +
                    "fechaHora = " + dto.getFechaHora() + "<br>" +
                    "motivo = " + dto.getMotivo() + "<br>" +
                    "especialidad = " + dto.getEspecialidad() + "<br><br>";

            citaFacade.crearCita(dto);

            return info + "<h2 style='color:green'>CITA CREADA OK</h2>" +
                    "<a href='/citas/ver?pacienteId=" + dto.getPacienteId() + "'>Ver citas</a>";

        } catch (Exception e) {
            String error = "<h2 style='color:red'>ERROR: " + e.getClass().getSimpleName() + "</h2>" +
                    "<p>Mensaje: " + e.getMessage() + "</p>";
            if (e.getCause() != null) {
                error += "<p>Causa: " + e.getCause().getMessage() + "</p>";
            }
            error += "<pre>";
            for (StackTraceElement el : e.getStackTrace()) {
                if (el.getClassName().contains("gestionHospitalaria")) {
                    error += el.toString() + "\n";
                }
            }
            error += "</pre>";
            error += "<a href='/citas/crear'>Volver</a>";
            return error;
        }
    }

    @GetMapping("/ver")
    public String verCitas(@RequestParam("pacienteId") Long pacienteId, Model model) {
        List<CitaDTO> citas = citaFacade.obtenerCitasPaciente(pacienteId);
        model.addAttribute("citas", citas);
        return "ver-citas";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "FUNCIONA";
    }
}