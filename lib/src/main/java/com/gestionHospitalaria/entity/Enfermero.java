package com.gestionHospitalaria.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Enfermero/a.
 *
 * Sigue el mismo patrón que {@link Recepcionista}: tabla propia,
 * pero el enum de rol vive en {@link Paciente.Rol}.
 *
 * Los enfermeros pueden registrar constantes vitales de los pacientes
 * (ver {@link ConstanteVital}) para que el médico disponga de información
 * actualizada antes de la consulta.
 */
@Entity
@Table(name = "enfermeros")
public class Enfermero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos personales
    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido1;

    @Column(length = 50)
    private String apellido2;

    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String sexo;

    // Contacto
    @Column(length = 20)
    private String telefono;

    @Column(unique = true, length = 100)
    private String email;

    // Información profesional
    @Column(length = 50)
    private String unidad; // ej: "Cardiología", "UCI", "Planta 3"

    @Column(length = 50)
    private String turno; // MAÑANA, TARDE, NOCHE, ROTATIVO

    @Column(length = 20)
    private String numeroColegiado;

    private LocalDate fechaIncorporacion;

    // Seguridad
    @Column(length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Paciente.Rol rol;

    // Administrativos
    private Boolean activo;

    // Fechas de control
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    // Relaciones
    @OneToMany(mappedBy = "enfermero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConstanteVital> constantesRegistradas = new ArrayList<>();

    public Enfermero() {}

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.rol == null) this.rol = Paciente.Rol.ENFERMERO;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }

    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public String getNumeroColegiado() { return numeroColegiado; }
    public void setNumeroColegiado(String numeroColegiado) { this.numeroColegiado = numeroColegiado; }

    public LocalDate getFechaIncorporacion() { return fechaIncorporacion; }
    public void setFechaIncorporacion(LocalDate fechaIncorporacion) { this.fechaIncorporacion = fechaIncorporacion; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Paciente.Rol getRol() { return rol; }
    public void setRol(Paciente.Rol rol) { this.rol = rol; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public List<ConstanteVital> getConstantesRegistradas() { return constantesRegistradas; }
    public void setConstantesRegistradas(List<ConstanteVital> constantesRegistradas) {
        this.constantesRegistradas = constantesRegistradas;
    }
}