package com.gestionHospitalaria.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pacientes")
public class Paciente {

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
    private String numeroHistoriaClinica;

    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String sexo;

    @Column(length = 20)
    private String genero;

    @Column(length = 30)
    private String nacionalidad;

    // Contacto
    @Column(length = 20)
    private String telefono;

    @Column(length = 20)
    private String telefonoEmergencia;

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

    @Column(length = 50)
    private String pais;

    // Seguridad
    @Column(length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Rol rol;

    public enum Rol {
        PACIENTE, MEDICO, ADMIN
    }

    // Información médica básica
    @Column(length = 10)
    private String grupoSanguineo;

    private Double altura;
    private Double peso;

    @Column(length = 500)
    private String alergias;

    @Column(length = 1000)
    private String enfermedadesPrevias;

    @Column(length = 1000)
    private String medicacionActual;

    @Column(length = 1000)
    private String antecedentesFamiliares;

    @Column(length = 1000)
    private String observacionesMedicas;

    @Column(length = 20)
    private String estadoCivil;

    @Column(length = 50)
    private String ocupacion;

    @Column(length = 100)
    private String aseguradora;

    @Column(length = 50)
    private String numeroPoliza;

    // Persona de contacto de emergencia
    @Column(length = 100)
    private String nombreContactoEmergencia;

    @Column(length = 50)
    private String parentescoContactoEmergencia;

    // Administrativos
    private Boolean activo;
    private Boolean tieneSeguroPrivado;
    private Boolean consentimientoProteccionDatos;
    private Boolean consentimientoTratamientoDatos;

    // Fechas de control
    private LocalDate fechaAlta;
    private LocalDate fechaUltimaRevision;

    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    // Relaciones
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cita> citas = new ArrayList<>();

    public Paciente() {
    }

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.tieneSeguroPrivado == null) this.tieneSeguroPrivado = false;
        if (this.consentimientoProteccionDatos == null) this.consentimientoProteccionDatos = false;
        if (this.consentimientoTratamientoDatos == null) this.consentimientoTratamientoDatos = false;
        if (this.rol == null) this.rol = Rol.PACIENTE;
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

    public String getNumeroHistoriaClinica() { return numeroHistoriaClinica; }
    public void setNumeroHistoriaClinica(String numeroHistoriaClinica) { this.numeroHistoriaClinica = numeroHistoriaClinica; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTelefonoEmergencia() { return telefonoEmergencia; }
    public void setTelefonoEmergencia(String telefonoEmergencia) { this.telefonoEmergencia = telefonoEmergencia; }

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

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

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

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getAseguradora() { return aseguradora; }
    public void setAseguradora(String aseguradora) { this.aseguradora = aseguradora; }

    public String getNumeroPoliza() { return numeroPoliza; }
    public void setNumeroPoliza(String numeroPoliza) { this.numeroPoliza = numeroPoliza; }

    public String getNombreContactoEmergencia() { return nombreContactoEmergencia; }
    public void setNombreContactoEmergencia(String nombreContactoEmergencia) { this.nombreContactoEmergencia = nombreContactoEmergencia; }

    public String getParentescoContactoEmergencia() { return parentescoContactoEmergencia; }
    public void setParentescoContactoEmergencia(String parentescoContactoEmergencia) { this.parentescoContactoEmergencia = parentescoContactoEmergencia; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Boolean getTieneSeguroPrivado() { return tieneSeguroPrivado; }
    public void setTieneSeguroPrivado(Boolean tieneSeguroPrivado) { this.tieneSeguroPrivado = tieneSeguroPrivado; }

    public Boolean getConsentimientoProteccionDatos() { return consentimientoProteccionDatos; }
    public void setConsentimientoProteccionDatos(Boolean v) { this.consentimientoProteccionDatos = v; }

    public Boolean getConsentimientoTratamientoDatos() { return consentimientoTratamientoDatos; }
    public void setConsentimientoTratamientoDatos(Boolean v) { this.consentimientoTratamientoDatos = v; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    public LocalDate getFechaUltimaRevision() { return fechaUltimaRevision; }
    public void setFechaUltimaRevision(LocalDate fechaUltimaRevision) { this.fechaUltimaRevision = fechaUltimaRevision; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }
}