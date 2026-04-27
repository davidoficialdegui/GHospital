package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
    Optional<Paciente> findByEmail(String email);
    long countByRol(Paciente.Rol rol);
}