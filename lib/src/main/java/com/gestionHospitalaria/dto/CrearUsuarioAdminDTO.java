package com.gestionHospitalaria.dto;

/**
 * DTO para que el administrador cree un nuevo usuario del sistema.
 * El campo 'tipo' determina en qué tabla se almacena: PACIENTE, MEDICO o RECEPCIONISTA.
 */
public class CrearUsuarioAdminDTO {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String dni;
    private String email;
    private String password;
    private String tipo; // PACIENTE, MEDICO, RECEPCIONISTA
    private String especialidad; // solo para MEDICO

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }

    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
