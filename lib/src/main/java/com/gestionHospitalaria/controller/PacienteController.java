package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import com.gestionHospitalaria.dto.LoginDTO;
import com.gestionHospitalaria.dto.RegistroPacienteDTO;
import com.gestionHospitalaria.entity.Paciente;
import com.gestionHospitalaria.facade.PacienteFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Tag(name = "Pacientes", description = "Registro, autenticación e historial de pacientes")
@Controller
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteFacade pacienteFacade;

    @Operation(summary = "Registrar un nuevo paciente", description = "Crea una cuenta de paciente y redirige al login")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "Redirige a /login si el registro es exitoso"),
        @ApiResponse(responseCode = "302", description = "Redirige a /registro con error si el email o DNI ya existen")
    })
    @PostMapping("/registro")
    public String registrar(@ModelAttribute RegistroPacienteDTO dto,
                            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            pacienteFacade.registrar(dto);
            ra.addFlashAttribute("mensaje", "Registro completado. Ya puedes iniciar sesión.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y redirige a su panel según el rol (ADMIN, MEDICO, PACIENTE, etc.)")
    @ApiResponses({
        @ApiResponse(responseCode = "302", description = "Redirige al panel del rol correspondiente"),
        @ApiResponse(responseCode = "302", description = "Redirige a /login con error si las credenciales son incorrectas")
    })
    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO dto,
                        org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                        HttpSession session) {
        try {
            String resultado = pacienteFacade.login(dto);
            // resultado = "ROL|ID|NOMBRE"
            String[] partes = resultado.split("\\|");
            String rol    = partes[0];
            String id     = partes[1];
            String nombre = partes.length > 2 ? partes[2] : "";

            // Guardar en sesión para control de acceso
            session.setAttribute("sessionUserRole", rol);
            session.setAttribute("sessionUserId",   Long.parseLong(id));
            session.setAttribute("sessionUserName", nombre);

            switch (rol) {
                case "ADMIN":
                    return "redirect:/admin/usuarios";
                case "RECEPCIONISTA":
                    return "redirect:/citas";
                case "MEDICO":
                    return "redirect:/medico/agenda?medicoId=" + id;
                case "ENFERMERO":
                    return "redirect:/enfermero/inicio";
                case "FARMACEUTICO":
                    return "redirect:/farmaceutico/inicio";
                case "PACIENTE":
                default:
                    return "redirect:/paciente/historial?pacienteId=" + id;
            }
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @Operation(summary = "Historial médico de un paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial médico"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    @GetMapping("/{id}/historial")
    @ResponseBody
    public HistorialMedicoDTO historial(
            @Parameter(description = "ID del paciente") @PathVariable("id") Long id) {
        return pacienteFacade.obtenerHistorial(id);
    }
}