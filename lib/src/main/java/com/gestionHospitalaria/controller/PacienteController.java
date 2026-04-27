package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import com.gestionHospitalaria.dto.LoginDTO;
import com.gestionHospitalaria.dto.RegistroPacienteDTO;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.facade.PacienteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteFacade pacienteFacade;

    @PostMapping("/registro")
    @ResponseBody
    public Paciente registrar(@ModelAttribute RegistroPacienteDTO dto) {
        return pacienteFacade.registrar(dto);
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO dto,
                        org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            String resultado = pacienteFacade.login(dto);
            // resultado = "ROL|ID|NOMBRE"
            String[] partes = resultado.split("\\|");
            String rol = partes[0];
            String id  = partes[1];

            switch (rol) {
                case "ADMIN":
                    return "redirect:/admin/usuarios";
                case "RECEPCIONISTA":
                    return "redirect:/citas/crear";
                case "MEDICO":
                    return "redirect:/medico/agenda?medicoId=" + id;
                case "PACIENTE":
                default:
                    return "redirect:/paciente/historial?pacienteId=" + id;
            }
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/{id}/historial")
    @ResponseBody
    public HistorialMedicoDTO historial(@PathVariable("id") Long id) {
        return pacienteFacade.obtenerHistorial(id);
    }
}