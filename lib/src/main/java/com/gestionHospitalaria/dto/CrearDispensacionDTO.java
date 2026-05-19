package com.gestionHospitalaria.dto;

public class CrearDispensacionDTO {

    private Long recetaId;
    private Long farmaceuticoId;
    private Integer cantidadDispensada;
    private String estado;
    private String observaciones;

    public Long getRecetaId() { return recetaId; }
    public void setRecetaId(Long recetaId) { this.recetaId = recetaId; }

    public Long getFarmaceuticoId() { return farmaceuticoId; }
    public void setFarmaceuticoId(Long farmaceuticoId) { this.farmaceuticoId = farmaceuticoId; }

    public Integer getCantidadDispensada() { return cantidadDispensada; }
    public void setCantidadDispensada(Integer cantidadDispensada) { this.cantidadDispensada = cantidadDispensada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
