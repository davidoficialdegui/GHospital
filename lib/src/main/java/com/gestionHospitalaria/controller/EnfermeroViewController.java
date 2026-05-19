package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.ConstanteVitalDTO;
import com.gestionHospitalaria.dto.CrearConstanteVitalDTO;
import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.facade.ConstanteVitalFacade;
import com.gestionHospitalaria.facade.RecetaFacade;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Vistas Thymeleaf para el rol ENFERMERO.
 *
 *   GET  /enfermero/inicio               -> pantalla principal (buscar paciente)
 *   GET  /enfermero/constantes/nueva     -> formulario de toma de constantes
 *   POST /enfermero/constantes/nueva     -> guardar toma
 *   GET  /enfermero/constantes/ver       -> ver historial de constantes de un paciente
 */
@Controller
@RequestMapping("/enfermero")
public class EnfermeroViewController {

    private static final Logger logger = LoggerFactory.getLogger(EnfermeroViewController.class);

    @Autowired
    private ConstanteVitalFacade constanteVitalFacade;
    @Autowired
    private RecetaFacade recetaFacade;

    /** Pantalla de inicio para enfermería: pide el ID de paciente. */
    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {
        Long enfermeroId = (Long) session.getAttribute("sessionUserId");
        String nombre   = (String) session.getAttribute("sessionUserName");
        model.addAttribute("enfermeroId", enfermeroId);
        model.addAttribute("enfermeroNombre", nombre);
        return "enfermero-inicio";
    }

    /** Formulario de nueva toma de constantes. */
    @GetMapping("/constantes/nueva")
    public String formNuevaToma(
            @RequestParam(name = "pacienteId", required = false) Long pacienteId,
            @RequestParam(name = "enfermeroId", required = false) Long enfermeroId,
            HttpSession session,
            Model model) {
        Long sessionId = (Long) session.getAttribute("sessionUserId");
        String rol     = (String) session.getAttribute("sessionUserRole");
        if ("ENFERMERO".equals(rol) && sessionId != null) {
            enfermeroId = sessionId;
        }
        logger.info("GET /enfermero/constantes/nueva pacienteId={} enfermeroId={}", pacienteId, enfermeroId);
        model.addAttribute("pacienteId", pacienteId);
        model.addAttribute("enfermeroId", enfermeroId);
        return "nueva-constante-vital";
    }

    /** Procesar registro de constantes. */
    @PostMapping("/constantes/nueva")
    public String registrarToma(@ModelAttribute CrearConstanteVitalDTO dto,
                                HttpSession session,
                                Model model) {
        // Si hay sesión activa de enfermero, forzamos el enfermeroId desde la sesión
        Long sessionId = (Long) session.getAttribute("sessionUserId");
        String rol     = (String) session.getAttribute("sessionUserRole");
        if ("ENFERMERO".equals(rol) && sessionId != null) {
            dto.setEnfermeroId(sessionId);
        }
        logger.info("POST /enfermero/constantes/nueva pacienteId={}", dto.getPacienteId());
        try {
            ConstanteVitalDTO guardada = constanteVitalFacade.registrar(dto);
            model.addAttribute("exito", true);
            model.addAttribute("constante", guardada);
            logger.info("Constantes registradas con id={}", guardada.getId());
        } catch (Exception e) {
            logger.error("Error al registrar constantes: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pacienteId", dto.getPacienteId());
            model.addAttribute("enfermeroId", dto.getEnfermeroId());
        }
        return "nueva-constante-vital";
    }

    /** Ver el historial de constantes de un paciente. */
    @GetMapping("/constantes/ver")
    public String verConstantes(
            @RequestParam(name = "pacienteId") Long pacienteId,
            Model model) {
        logger.info("GET /enfermero/constantes/ver pacienteId={}", pacienteId);
        try {
            List<ConstanteVitalDTO> tomas = constanteVitalFacade.obtenerPorPaciente(pacienteId);
            model.addAttribute("tomas", tomas);
        } catch (Exception e) {
            logger.error("Error al obtener constantes: {}", e.getMessage());
            model.addAttribute("tomas", java.util.Collections.emptyList());
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("pacienteId", pacienteId);
        return "ver-constantes-paciente";
    }
    
    @GetMapping("/tratamientos/ver")
    public String verTratamientos(
            @RequestParam(name = "pacienteId") Long pacienteId,
            Model model) {
        logger.info("GET /enfermero/tratamientos/ver pacienteId={}", pacienteId);
        try {
            List<RecetaDTO> recetas = recetaFacade.obtenerRecetasPaciente(pacienteId);
            model.addAttribute("recetas", recetas);
        } catch (Exception e) {
            logger.error("Error al obtener tratamientos: {}", e.getMessage());
            model.addAttribute("recetas", java.util.Collections.emptyList());
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("pacienteId", pacienteId);
        return "ver-tratamientos-enfermero";
    }
}