package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.EditarMedicoDTO;
import com.gestionHospitalaria.dto.EditarPacienteDTO;
import com.gestionHospitalaria.dto.EditarRecepcionistaDTO;
import com.gestionHospitalaria.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("pacientes", adminService.listarPacientes());
        model.addAttribute("medicos", adminService.listarMedicos());
        model.addAttribute("recepcionistas", adminService.listarRecepcionistas());
        return "admin-usuarios";
    }

    @GetMapping("/editar/paciente/{id}")
    public String mostrarEditarPaciente(@PathVariable Long id, Model model) {
        model.addAttribute("paciente", adminService.obtenerPaciente(id));
        return "admin-editar-paciente";
    }

    @PostMapping("/editar/paciente/{id}")
    public String editarPaciente(@PathVariable Long id, @ModelAttribute EditarPacienteDTO dto, Model model) {
        try {
            adminService.editarPaciente(id, dto);
            model.addAttribute("mensaje", "Paciente actualizado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("paciente", adminService.obtenerPaciente(id));
        return "admin-editar-paciente";
    }

    @GetMapping("/editar/medico/{id}")
    public String mostrarEditarMedico(@PathVariable Long id, Model model) {
        model.addAttribute("medico", adminService.obtenerMedico(id));
        return "admin-editar-medico";
    }

    @PostMapping("/editar/medico/{id}")
    public String editarMedico(@PathVariable Long id, @ModelAttribute EditarMedicoDTO dto, Model model) {
        try {
            adminService.editarMedico(id, dto);
            model.addAttribute("mensaje", "Médico actualizado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("medico", adminService.obtenerMedico(id));
        return "admin-editar-medico";
    }

    @GetMapping("/editar/recepcionista/{id}")
    public String mostrarEditarRecepcionista(@PathVariable Long id, Model model) {
        model.addAttribute("recepcionista", adminService.obtenerRecepcionista(id));
        return "admin-editar-recepcionista";
    }

    @PostMapping("/editar/recepcionista/{id}")
    public String editarRecepcionista(@PathVariable Long id, @ModelAttribute EditarRecepcionistaDTO dto, Model model) {
        try {
            adminService.editarRecepcionista(id, dto);
            model.addAttribute("mensaje", "Recepcionista actualizado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("recepcionista", adminService.obtenerRecepcionista(id));
        return "admin-editar-recepcionista";
    }

    @PostMapping("/eliminar/paciente/{id}")
    public String eliminarPaciente(@PathVariable Long id, Model model) {
        try {
            adminService.eliminarPaciente(id);
            model.addAttribute("mensaje", "Paciente eliminado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("pacientes", adminService.listarPacientes());
        model.addAttribute("medicos", adminService.listarMedicos());
        model.addAttribute("recepcionistas", adminService.listarRecepcionistas());
        return "admin-usuarios";
    }

    @PostMapping("/eliminar/medico/{id}")
    public String eliminarMedico(@PathVariable Long id, Model model) {
        try {
            adminService.eliminarMedico(id);
            model.addAttribute("mensaje", "Médico eliminado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("pacientes", adminService.listarPacientes());
        model.addAttribute("medicos", adminService.listarMedicos());
        model.addAttribute("recepcionistas", adminService.listarRecepcionistas());
        return "admin-usuarios";
    }

    @PostMapping("/eliminar/recepcionista/{id}")
    public String eliminarRecepcionista(@PathVariable Long id, Model model) {
        try {
            adminService.eliminarRecepcionista(id);
            model.addAttribute("mensaje", "Recepcionista eliminado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("pacientes", adminService.listarPacientes());
        model.addAttribute("medicos", adminService.listarMedicos());
        model.addAttribute("recepcionistas", adminService.listarRecepcionistas());
        return "admin-usuarios";
    }
}
