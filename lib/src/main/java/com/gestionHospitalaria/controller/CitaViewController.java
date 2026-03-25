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

    // 🔴 HU3 → Mostrar formulario
    @GetMapping("/crear")
    public String mostrarFormulario() {
        return "crearCita";
    }

    // 🔴 HU3 → Procesar formulario
    @PostMapping("/crear")
    public String crearCita(@ModelAttribute CrearCitaDTO dto) {
        citaFacade.crearCita(dto);
        return "redirect:/citas/ver?pacienteId=" + dto.getPacienteId();
    }

    // 🔵 HU4 → Ver citas
    @GetMapping("/ver")
    public String verCitas(@RequestParam Long pacienteId, Model model) {
        List<CitaDTO> citas = citaFacade.obtenerCitasPaciente(pacienteId);
        model.addAttribute("citas", citas);
        return "verCitas";
    }

    // 🧪 TEST (para comprobar que el controller funciona)
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "FUNCIONA";
    }
}