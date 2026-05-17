package com.gestionHospitalaria.facade;

import com.gestionHospitalaria.dto.CrearRecetaDTO;
import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecetaFacade {

    @Autowired
    private RecetaService recetaService;

    public RecetaDTO crearReceta(CrearRecetaDTO dto) {
        return recetaService.crearReceta(dto);
    }

    public List<RecetaDTO> obtenerRecetasPaciente(Long pacienteId) {
        return recetaService.obtenerRecetasPaciente(pacienteId);
    }

    public RecetaDTO obtenerRecetaPorId(Long id) {
        return recetaService.obtenerRecetaPorId(id);
    }
}