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
    @ResponseBody
    public String login(@ModelAttribute LoginDTO dto) {
        return pacienteFacade.login(dto);
    }

    @GetMapping("/{id}/historial")
    @ResponseBody
    public HistorialMedicoDTO historial(@PathVariable Long id) {
        return pacienteFacade.obtenerHistorial(id);
    }
}