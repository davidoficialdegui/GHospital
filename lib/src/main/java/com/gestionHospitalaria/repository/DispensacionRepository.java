package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Dispensacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DispensacionRepository extends JpaRepository<Dispensacion, Long> {

    List<Dispensacion> findByRecetaPacienteIdOrderByFechaDispensacionDesc(Long pacienteId);

    List<Dispensacion> findByFarmaceuticoIdOrderByFechaDispensacionDesc(Long farmaceuticoId);

    List<Dispensacion> findByRecetaIdOrderByFechaDispensacionDesc(Long recetaId);
}
