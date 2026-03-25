package com.gestionHospitalaria.dto;

import java.time.LocalDateTime;

public class CrearCitaDTO {

    private Long pacienteId;
    private Long medicoId;
    private String fechaHora;
    private String motivo;
    private String especialidad;

    public CrearCitaDTO() {}

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public LocalDateTime getFechaHoraParsed() {
        if (fechaHora == null || fechaHora.isEmpty()) return null;
        return LocalDateTime.parse(fechaHora);
    }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}