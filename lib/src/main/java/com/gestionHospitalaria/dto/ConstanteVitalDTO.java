package com.gestionHospitalaria.dto;

import java.time.LocalDateTime;

/**
 * DTO de salida para mostrar una toma de constantes vitales en la vista
 * (tanto a enfermería como a los médicos en el historial del paciente).
 */
public class ConstanteVitalDTO {

    private Long id;

    private Long   pacienteId;
    private String pacienteNombre;

    private Long   enfermeroId;
    private String enfermeroNombre;

    private Integer tensionSistolica;
    private Integer tensionDiastolica;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private Double  temperatura;
    private Integer saturacionOxigeno;
    private Integer glucemia;
    private Double  peso;

    private String observaciones;

    private LocalDateTime fechaRegistro;

    public ConstanteVitalDTO() {}

    /** Helper para la vista: "120/80" o "—" si no se midió. */
    public String getTensionFormateada() {
        if (tensionSistolica == null && tensionDiastolica == null) return "—";
        String s = tensionSistolica  != null ? tensionSistolica.toString()  : "?";
        String d = tensionDiastolica != null ? tensionDiastolica.toString() : "?";
        return s + "/" + d;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }

    public Long getEnfermeroId() { return enfermeroId; }
    public void setEnfermeroId(Long enfermeroId) { this.enfermeroId = enfermeroId; }

    public String getEnfermeroNombre() { return enfermeroNombre; }
    public void setEnfermeroNombre(String enfermeroNombre) { this.enfermeroNombre = enfermeroNombre; }

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