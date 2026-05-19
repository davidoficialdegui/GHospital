package com.gestionHospitalaria.dto;

/**
 * DTO de entrada para que el enfermero registre una toma de constantes vitales.
 *
 * Todos los campos clínicos son opcionales individualmente (puede ser que en
 * una toma rápida solo se mida tensión, por ejemplo), pero el servicio exige
 * que al menos UNA constante esté informada para que la toma tenga sentido.
 */
public class CrearConstanteVitalDTO {

    private Long pacienteId;
    private Long enfermeroId;

    private Integer tensionSistolica;
    private Integer tensionDiastolica;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private Double  temperatura;
    private Integer saturacionOxigeno;
    private Integer glucemia;
    private Double  peso;

    private String observaciones;

    public CrearConstanteVitalDTO() {}

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getEnfermeroId() { return enfermeroId; }
    public void setEnfermeroId(Long enfermeroId) { this.enfermeroId = enfermeroId; }

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
}