package com.gestionHospitalaria.repository;

import com.gestionHospitalaria.entity.Farmaceutico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmaceuticoRepository extends JpaRepository<Farmaceutico, Long> {
    Optional<Farmaceutico> findByEmail(String email);
}
