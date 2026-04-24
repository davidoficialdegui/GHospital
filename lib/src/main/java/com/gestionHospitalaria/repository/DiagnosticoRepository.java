package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    List<Diagnostico> findByPacienteIdOrderByFechaDiagnosticoDesc(Long pacienteId);

    List<Diagnostico> findByMedicoIdOrderByFechaDiagnosticoDesc(Long medicoId);
}