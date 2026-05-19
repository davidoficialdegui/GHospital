package com.gestionHospitalaria.facade;

import com.gestionHospitalaria.dto.ConstanteVitalDTO;
import com.gestionHospitalaria.dto.CrearConstanteVitalDTO;
import com.gestionHospitalaria.service.ConstanteVitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConstanteVitalFacade {

    @Autowired
    private ConstanteVitalService service;

    public ConstanteVitalDTO registrar(CrearConstanteVitalDTO dto) {
        return service.registrar(dto);
    }

    public List<ConstanteVitalDTO> obtenerPorPaciente(Long pacienteId) {
        return service.obtenerPorPaciente(pacienteId);
    }

    public ConstanteVitalDTO obtenerUltimaPorPaciente(Long pacienteId) {
        return service.obtenerUltimaPorPaciente(pacienteId);
    }
}