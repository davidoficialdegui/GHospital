package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.ConstanteVital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstanteVitalRepository extends JpaRepository<ConstanteVital, Long> {

    /** Todas las tomas de un paciente, más recientes primero. */
    List<ConstanteVital> findByPacienteIdOrderByFechaRegistroDesc(Long pacienteId);

    /** Todas las tomas registradas por un enfermero, más recientes primero. */
    List<ConstanteVital> findByEnfermeroIdOrderByFechaRegistroDesc(Long enfermeroId);
}