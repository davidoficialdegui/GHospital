package com.gestionHospitalaria.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dispensaciones")
public class Dispensacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", nullable = false)
    private Receta receta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmaceutico_id", nullable = false)
    private Farmaceutico farmaceutico;

    @Column(nullable = false)
    private Integer cantidadDispensada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDispensacion estado;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false)
    private LocalDateTime fechaDispensacion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public enum EstadoDispensacion {
        DISPENSADO, PARCIAL, PENDIENTE
    }

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.fechaDispensacion == null) {
            this.fechaDispensacion = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = EstadoDispensacion.DISPENSADO;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Receta getReceta() { return receta; }
    public void setReceta(Receta receta) { this.receta = receta; }

    public Farmaceutico getFarmaceutico() { return farmaceutico; }
    public void setFarmaceutico(Farmaceutico farmaceutico) { this.farmaceutico = farmaceutico; }

    public Integer getCantidadDispensada() { return cantidadDispensada; }
    public void setCantidadDispensada(Integer cantidadDispensada) { this.cantidadDispensada = cantidadDispensada; }

    public EstadoDispensacion getEstado() { return estado; }
    public void setEstado(EstadoDispensacion estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaDispensacion() { return fechaDispensacion; }
    public void setFechaDispensacion(LocalDateTime fechaDispensacion) { this.fechaDispensacion = fechaDispensacion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
