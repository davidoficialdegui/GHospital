package com.gestionHospitalaria.dto;

import java.time.LocalDateTime;

public class DispensacionDTO {

    private Long id;
    private Long recetaId;
    private String medicamento;
    private String dosis;
    private Long pacienteId;
    private String pacienteNombre;
    private Long farmaceuticoId;
    private String farmaceuticoNombre;
    private Integer cantidadDispensada;
    private String estado;
    private String observaciones;
    private LocalDateTime fechaDispensacion;
    private LocalDateTime fechaCreacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecetaId() { return recetaId; }
    public void setRecetaId(Long recetaId) { this.recetaId = recetaId; }

    public String getMedicamento() { return medicamento; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }

    public Long getFarmaceuticoId() { return farmaceuticoId; }
    public void setFarmaceuticoId(Long farmaceuticoId) { this.farmaceuticoId = farmaceuticoId; }

    public String getFarmaceuticoNombre() { return farmaceuticoNombre; }
    public void setFarmaceuticoNombre(String farmaceuticoNombre) { this.farmaceuticoNombre = farmaceuticoNombre; }

    public Integer getCantidadDispensada() { return cantidadDispensada; }
    public void setCantidadDispensada(Integer cantidadDispensada) { this.cantidadDispensada = cantidadDispensada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaDispensacion() { return fechaDispensacion; }
    public void setFechaDispensacion(LocalDateTime fechaDispensacion) { this.fechaDispensacion = fechaDispensacion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
