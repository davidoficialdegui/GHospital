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
    public String crearCita(@ModelAttribute CrearCitaDTO dto,
                            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            citaFacade.crearCita(dto);
            ra.addFlashAttribute("mensaje", "Cita creada correctamente.");
            return "redirect:/citas/ver?pacienteId=" + dto.getPacienteId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/citas/crear";
        }
    }

    @GetMapping("/ver")
    public String verCitas(@RequestParam("pacienteId") Long pacienteId, Model model) {
        List<CitaDTO> citas = citaFacade.obtenerCitasPaciente(pacienteId);
        model.addAttribute("citas", citas);
        model.addAttribute("pacienteId", pacienteId);
        return "ver-citas";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "FUNCIONA";
    }
    
    @PostMapping("/{citaId}/cancelar")
    public String cancelarCitaVista(
    		@PathVariable("citaId") Long citaId,         
            @RequestParam("pacienteId") Long pacienteId, 
            Model model) {
        try {
            citaFacade.cancelarCita(citaId, pacienteId);
            model.addAttribute("mensaje", "Cita cancelada correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        List<CitaDTO> citas = citaFacade.obtenerCitasPaciente(pacienteId);
        model.addAttribute("citas", citas);
        model.addAttribute("pacienteId", pacienteId);
        return "ver-citas";
    }
}