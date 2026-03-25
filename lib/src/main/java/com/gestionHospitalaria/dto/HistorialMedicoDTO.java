package com.gestionHospitalaria.dto;

public class HistorialMedicoDTO {

    private String nombreCompleto;
    private String grupoSanguineo;
    private Double altura;
    private Double peso;
    private String alergias;
    private String enfermedadesPrevias;
    private String medicacionActual;
    private String antecedentesFamiliares;
    private String observacionesMedicas;

    public HistorialMedicoDTO() {}

    // getters y setters

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getGrupoSanguineo() { return grupoSanguineo; }
    public void setGrupoSanguineo(String grupoSanguineo) { this.grupoSanguineo = grupoSanguineo; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getEnfermedadesPrevias() { return enfermedadesPrevias; }
    public void setEnfermedadesPrevias(String enfermedadesPrevias) { this.enfermedadesPrevias = enfermedadesPrevias; }

    public String getMedicacionActual() { return medicacionActual; }
    public void setMedicacionActual(String medicacionActual) { this.medicacionActual = medicacionActual; }

    public String getAntecedentesFamiliares() { return antecedentesFamiliares; }
    public void setAntecedentesFamiliares(String antecedentesFamiliares) { this.antecedentesFamiliares = antecedentesFamiliares; }

    public String getObservacionesMedicas() { return observacionesMedicas; }
    public void setObservacionesMedicas(String observacionesMedicas) { this.observacionesMedicas = observacionesMedicas; }
}