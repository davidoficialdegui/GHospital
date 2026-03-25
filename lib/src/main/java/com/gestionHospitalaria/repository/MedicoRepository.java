package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}