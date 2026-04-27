package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.entity.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long> {
    List<Recepcionista> findByRol(Paciente.Rol rol);
    Optional<Recepcionista> findByEmail(String email);
    long countByRol(Paciente.Rol rol);
}
