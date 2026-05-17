package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta, Long> {

    List<Receta> findByPacienteIdOrderByFechaEmisionDesc(Long pacienteId);

    List<Receta> findByMedicoIdOrderByFechaEmisionDesc(Long medicoId);
}