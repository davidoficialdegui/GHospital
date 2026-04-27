package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPacienteId(Long pacienteId);

    List<Cita> findByMedicoIdAndFechaHoraBetween(
            Long medicoId,
            LocalDateTime inicio,
            LocalDateTime fin
    );

    long countByEstado(Cita.EstadoCita estado);
}