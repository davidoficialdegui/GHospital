package com.gestionHospitalaria.facade;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.dto.CrearCitaDTO;
import com.gestionHospitalaria.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CitaFacade {

    @Autowired
    private CitaService citaService;

    public CitaDTO crearCita(CrearCitaDTO dto) {
        return citaService.crearCita(dto);
    }

    
    public List<CitaDTO> obtenerCitasPaciente(Long pacienteId) {
        return citaService.obtenerCitasPaciente(pacienteId);
    }
    
    public List<CitaDTO> obtenerAgendaDelDia(Long medicoId) {
        return citaService.obtenerAgendaDelDia(medicoId);
    }
}