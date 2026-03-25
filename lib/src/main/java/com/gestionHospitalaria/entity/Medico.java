package com.gestionHospitalaria.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "medicos")
public class Medico {

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

    @Column(unique = true, length = 20)
    private String numeroColegiado;

    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String sexo;

    // Contacto
    @Column(length = 20)
    private String telefono;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 100)
    private String direccion;

    @Column(length = 50)
    private String ciudad;

    @Column(length = 50)
    private String provincia;

    @Column(length = 10)
    private String codigoPostal;

    // Información profesional
    @Column(nullable = false, length = 100)
    private String especialidad;

    @Column(length = 100)
    private String subespecialidad;

    @Column(length = 50)
    private String departamento;

    private Integer aniosExperiencia;

    private LocalDate fechaIncorporacion;

    @Column(length = 50)
    private String turno; // MAÑANA, TARDE, NOCHE, ROTATIVO

    @Column(length = 500)
    private String descripcionProfesional;

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

    @JsonIgnore
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cita> citas = new ArrayList<>();

    public Medico() {}

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.rol == null) this.rol = Paciente.Rol.MEDICO;
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

    public String getNumeroColegiado() { return numeroColegiado; }
    public void setNumeroColegiado(String numeroColegiado) { this.numeroColegiado = numeroColegiado; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getSubespecialidad() { return subespecialidad; }
    public void setSubespecialidad(String subespecialidad) { this.subespecialidad = subespecialidad; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public Integer getAniosExperiencia() { return aniosExperiencia; }
    public void setAniosExperiencia(Integer aniosExperiencia) { this.aniosExperiencia = aniosExperiencia; }

    public LocalDate getFechaIncorporacion() { return fechaIncorporacion; }
    public void setFechaIncorporacion(LocalDate fechaIncorporacion) { this.fechaIncorporacion = fechaIncorporacion; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public String getDescripcionProfesional() { return descripcionProfesional; }
    public void setDescripcionProfesional(String descripcionProfesional) { this.descripcionProfesional = descripcionProfesional; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Paciente.Rol getRol() { return rol; }
    public void setRol(Paciente.Rol rol) { this.rol = rol; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }
}