package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Enfermero;
import com.gestionHospitalaria.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnfermeroRepository extends JpaRepository<Enfermero, Long> {

    Optional<Enfermero> findByEmail(String email);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

    long countByRol(Paciente.Rol rol);
}