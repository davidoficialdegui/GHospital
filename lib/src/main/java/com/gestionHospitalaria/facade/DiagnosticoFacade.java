package com.gestionHospitalaria.facade;

import com.gestionHospitalaria.dto.CrearDiagnosticoDTO;
import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.service.DiagnosticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiagnosticoFacade {

    @Autowired
    private DiagnosticoService diagnosticoService;

    public DiagnosticoDTO registrarDiagnostico(CrearDiagnosticoDTO dto) {
        return diagnosticoService.registrarDiagnostico(dto);
    }

    public List<DiagnosticoDTO> obtenerDiagnosticosPaciente(Long pacienteId) {
        return diagnosticoService.obtenerDiagnosticosPaciente(pacienteId);
    }
}