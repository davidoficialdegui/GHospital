package com.gestionHospitalaria.facade;

import com.gestionHospitalaria.dto.CrearDispensacionDTO;
import com.gestionHospitalaria.dto.DispensacionDTO;
import com.gestionHospitalaria.service.DispensacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DispensacionFacade {

    @Autowired
    private DispensacionService dispensacionService;

    public DispensacionDTO registrarDispensacion(CrearDispensacionDTO dto) {
        return dispensacionService.registrarDispensacion(dto);
    }

    public List<DispensacionDTO> obtenerPorPaciente(Long pacienteId) {
        return dispensacionService.obtenerPorPaciente(pacienteId);
    }

    public List<DispensacionDTO> obtenerPorFarmaceutico(Long farmaceuticoId) {
        return dispensacionService.obtenerPorFarmaceutico(farmaceuticoId);
    }

    public List<DispensacionDTO> obtenerPorReceta(Long recetaId) {
        return dispensacionService.obtenerPorReceta(recetaId);
    }
}
