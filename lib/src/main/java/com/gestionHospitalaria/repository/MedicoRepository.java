package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Medico;
import com.gestionHospitalaria.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByEmail(String email);
    long countByRol(Paciente.Rol rol);
}