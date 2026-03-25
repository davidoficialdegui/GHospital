package com.gestionHospitalaria.facade;

import com.gestionHospitalaria.dto.LoginDTO;
import com.gestionHospitalaria.dto.RegistroPacienteDTO;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PacienteFacade {

    @Autowired
    private PacienteService pacienteService;

    public Paciente registrar(RegistroPacienteDTO dto) {
        return pacienteService.registrar(dto);
    }

    public String login(LoginDTO dto) {
        return pacienteService.login(dto);
    }
}