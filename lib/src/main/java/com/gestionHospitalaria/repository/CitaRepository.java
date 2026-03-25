package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    //obtener todas las citas de un paciente
    List<Cita> findByPacienteId(Long pacienteId);

}