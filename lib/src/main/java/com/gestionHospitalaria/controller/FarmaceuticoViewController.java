package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CrearDispensacionDTO;
import com.gestionHospitalaria.dto.DispensacionDTO;
import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.facade.DispensacionFacade;
import com.gestionHospitalaria.facade.RecetaFacade;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Vistas Thymeleaf para el rol FARMACEUTICO.
 *
 *   GET  /farmaceutico/inicio              -> pantalla principal
 *   GET  /farmaceutico/recetas/buscar      -> buscar recetas por DNI del paciente
 *   GET  /farmaceutico/dispensar           -> formulario de registro de dispensación
 *   POST /farmaceutico/dispensar           -> guardar dispensación
 */
@Controller
@RequestMapping("/farmaceutico")
public class FarmaceuticoViewController {

    private static final Logger logger = LoggerFactory.getLogger(FarmaceuticoViewController.class);

    @Autowired
    private RecetaFacade recetaFacade;

    @Autowired
    private DispensacionFacade dispensacionFacade;

    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {
        String nombre = (String) session.getAttribute("sessionUserName");
        Long farmaceuticoId = (Long) session.getAttribute("sessionUserId");
        model.addAttribute("farmaceuticoNombre", nombre);
        model.addAttribute("farmaceuticoId", farmaceuticoId);
        return "farmaceutico-inicio";
    }

    @GetMapping("/recetas/buscar")
    public String buscarRecetas(
            @RequestParam(name = "dni", required = false) String dni,
            HttpSession session,
            Model model) {
        model.addAttribute("dni", dni);
        model.addAttribute("farmaceuticoId", session.getAttribute("sessionUserId"));
        if (dni != null && !dni.isBlank()) {
            logger.info("GET /farmaceutico/recetas/buscar dni={}", dni);
            try {
                List<RecetaDTO> recetas = recetaFacade.obtenerRecetasPorDniPaciente(dni);
                model.addAttribute("recetas", recetas);
                logger.info("Se encontraron {} recetas para DNI={}", recetas.size(), dni);
            } catch (Exception e) {
                logger.error("Error al buscar recetas por DNI={}: {}", dni, e.getMessage());
                model.addAttribute("recetas", Collections.emptyList());
                model.addAttribute("error", e.getMessage());
            }
        }
        return "farmaceutico-buscar-recetas";
    }

    @GetMapping("/dispensar")
    public String formDispensacion(
            @RequestParam(name = "recetaId", required = false) Long recetaId,
            HttpSession session,
            Model model) {
        Long farmaceuticoId = (Long) session.getAttribute("sessionUserId");
        model.addAttribute("recetaId", recetaId);
        model.addAttribute("farmaceuticoId", farmaceuticoId);
        return "farmaceutico-dispensar";
    }

    @PostMapping("/dispensar")
    public String registrarDispensacion(
            @ModelAttribute CrearDispensacionDTO dto,
            HttpSession session,
            Model model) {
        Long sessionId = (Long) session.getAttribute("sessionUserId");
        String rol = (String) session.getAttribute("sessionUserRole");
        if ("FARMACEUTICO".equals(rol) && sessionId != null) {
            dto.setFarmaceuticoId(sessionId);
        }
        logger.info("POST /farmaceutico/dispensar recetaId={} farmaceuticoId={}", dto.getRecetaId(), dto.getFarmaceuticoId());
        try {
            DispensacionDTO guardada = dispensacionFacade.registrarDispensacion(dto);
            model.addAttribute("exito", true);
            model.addAttribute("dispensacion", guardada);
            logger.info("Dispensación registrada con id={}", guardada.getId());
        } catch (Exception e) {
            logger.error("Error al registrar dispensación: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("recetaId", dto.getRecetaId());
            model.addAttribute("farmaceuticoId", dto.getFarmaceuticoId());
        }
        return "farmaceutico-dispensar";
    }
}
