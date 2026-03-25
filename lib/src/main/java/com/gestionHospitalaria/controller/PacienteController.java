package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.LoginDTO;
import com.gestionHospitalaria.dto.RegistroPacienteDTO;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.facade.PacienteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteFacade pacienteFacade;

    @PostMapping("/registro")
    public Paciente registrar(@RequestBody RegistroPacienteDTO dto) {
        return pacienteFacade.registrar(dto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO dto) {
        return pacienteFacade.login(dto);
    }
}