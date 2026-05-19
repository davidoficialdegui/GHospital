package com.gestionHospitalaria.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Constantes vitales registradas a un paciente por un enfermero.
 *
 * Cada registro es un "snapshot" puntual en el tiempo (toma de constantes)
 * y queda asociado tanto al paciente como al enfermero que lo realizó,
 * para trazabilidad clínica.
 *
 * El médico consulta los últimos registros desde el historial del paciente.
 */
@Entity
@Table(name = "constantes_vitales")
public class ConstanteVital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermero_id", nullable = false)
    private Enfermero enfermero;

    /** Tensión arterial sistólica (mmHg). */
    private Integer tensionSistolica;

    /** Tensión arterial diastólica (mmHg). */
    private Integer tensionDiastolica;

    /** Frecuencia cardíaca (latidos por minuto). */
    private Integer frecuenciaCardiaca;

    /** Frecuencia respiratoria (respiraciones por minuto). */
    private Integer frecuenciaRespiratoria;

    /** Temperatura corporal (ºC). */
    private Double temperatura;

    /** Saturación de oxígeno (%). */
    private Integer saturacionOxigeno;

    /** Glucemia capilar (mg/dL). Opcional. */
    private Integer glucemia;

    /** Peso (kg). Opcional, normalmente no se toma en cada visita. */
    private Double peso;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Enfermero getEnfermero() { return enfermero; }
    public void setEnfermero(Enfermero enfermero) { this.enfermero = enfermero; }

    public Integer getTensionSistolica() { return tensionSistolica; }
    public void setTensionSistolica(Integer tensionSistolica) { this.tensionSistolica = tensionSistolica; }

    public Integer getTensionDiastolica() { return tensionDiastolica; }
    public void setTensionDiastolica(Integer tensionDiastolica) { this.tensionDiastolica = tensionDiastolica; }

    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }

    public Integer getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public Integer getSaturacionOxigeno() { return saturacionOxigeno; }
    public void setSaturacionOxigeno(Integer saturacionOxigeno) { this.saturacionOxigeno = saturacionOxigeno; }

    public Integer getGlucemia() { return glucemia; }
    public void setGlucemia(Integer glucemia) { this.glucemia = glucemia; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}