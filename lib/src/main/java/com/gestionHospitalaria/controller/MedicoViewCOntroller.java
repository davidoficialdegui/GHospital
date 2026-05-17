package com.gestionHospitalaria.controller;

import com.gestionHospitalaria.dto.CitaDTO;
import com.gestionHospitalaria.dto.CrearDiagnosticoDTO;
import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import com.gestionHospitalaria.facade.CitaFacade;
import com.gestionHospitalaria.facade.DiagnosticoFacade;
import com.gestionHospitalaria.facade.PacienteFacade;
import com.gestionHospitalaria.service.InformeMedicoService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.gestionHospitalaria.dto.CrearRecetaDTO;
import com.gestionHospitalaria.dto.RecetaDTO;
import com.gestionHospitalaria.facade.RecetaFacade;


import java.util.List;

@Controller
@RequestMapping("/medico")
public class MedicoViewCOntroller {

    private static final Logger logger = LoggerFactory.getLogger(MedicoViewCOntroller.class);

    @Autowired
    private CitaFacade citaFacade;

    @Autowired
    private PacienteFacade pacienteFacade;

    @Autowired
    private DiagnosticoFacade diagnosticoFacade;
    
    @Autowired
    private InformeMedicoService informeMedicoService;
    
    @Autowired
    private RecetaFacade recetaFacade;

    /** GET /medico/agenda?medicoId=1 */
    @GetMapping("/agenda")
    public String verAgenda(
            @RequestParam(name = "medicoId", required = false) Long medicoId,
            HttpSession session,
            Model model) {
        // Un MEDICO solo puede ver su propia agenda
        Long sessionId = (Long) session.getAttribute("sessionUserId");
        String rol     = (String) session.getAttribute("sessionUserRole");
        if ("MEDICO".equals(rol) && sessionId != null) {
            medicoId = sessionId;
        }
        logger.info("GET /medico/agenda medicoId={}", medicoId);
        if (medicoId != null) {
            List<CitaDTO> citas = citaFacade.obtenerAgendaDelDia(medicoId);
            model.addAttribute("citas", citas);
            model.addAttribute("medicoId", medicoId);
        }
        return "agenda-medico";
    }

    /** GET /medico/historial?pacienteId=1 */
    @GetMapping("/historial")
    public String verHistorial(
            @RequestParam(name = "pacienteId", required = false) Long pacienteId,
            Model model) {
        logger.info("GET /medico/historial pacienteId={}", pacienteId);
        if (pacienteId != null) {
            HistorialMedicoDTO historial = pacienteFacade.obtenerHistorial(pacienteId);
            model.addAttribute("historial", historial);
            model.addAttribute("pacienteId", pacienteId);
            // También cargar diagnósticos previos del paciente
            List<DiagnosticoDTO> diagnosticos = diagnosticoFacade.obtenerDiagnosticosPaciente(pacienteId);
            model.addAttribute("diagnosticos", diagnosticos);
        }
        return "historial-paciente";
    }

    /** GET /medico/diagnostico/nuevo?pacienteId=1&medicoId=1 */
    @GetMapping("/diagnostico/nuevo")
    public String formNuevoDiagnostico(
            @RequestParam(name = "pacienteId", required = false) Long pacienteId,
            @RequestParam(name = "medicoId", required = false) Long medicoId,
            Model model) {
        logger.info("GET /medico/diagnostico/nuevo pacienteId={} medicoId={}", pacienteId, medicoId);
        model.addAttribute("pacienteId", pacienteId);
        model.addAttribute("medicoId", medicoId);
        return "nuevo-diagnostico";
    }

    /** POST /medico/diagnostico/nuevo */
    @PostMapping("/diagnostico/nuevo")
    public String registrarDiagnostico(@ModelAttribute CrearDiagnosticoDTO dto, Model model) {
        logger.info("POST /medico/diagnostico/nuevo pacienteId={}", dto.getPacienteId());
        try {
            DiagnosticoDTO guardado = diagnosticoFacade.registrarDiagnostico(dto);
            model.addAttribute("exito", true);
            model.addAttribute("diagnostico", guardado);
            logger.info("Diagnóstico registrado con id={}", guardado.getId());
        } catch (Exception e) {
            logger.error("Error al registrar diagnóstico: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pacienteId", dto.getPacienteId());
            model.addAttribute("medicoId", dto.getMedicoId());
        }
        return "nuevo-diagnostico";
    }
    
    @GetMapping("/informe/descargar")
    public ResponseEntity<byte[]> descargarInforme(
            @RequestParam("pacienteId") Long pacienteId) {
        logger.info("GET /medico/informe/descargar pacienteId={}", pacienteId);
        HistorialMedicoDTO historial = pacienteFacade.obtenerHistorial(pacienteId);
        List<DiagnosticoDTO> diagnosticos = diagnosticoFacade.obtenerDiagnosticosPaciente(pacienteId);
        byte[] pdf = informeMedicoService.generarInformePdf(historial, diagnosticos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"informe-paciente-" + pacienteId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
    
    @GetMapping("/receta/nueva")
    public String formNuevaReceta(
            @RequestParam(name = "pacienteId", required = false) Long pacienteId,
            @RequestParam(name = "medicoId", required = false) Long medicoId,
            HttpSession session,
            Model model) {
        Long sessionId = (Long) session.getAttribute("sessionUserId");
        String rol = (String) session.getAttribute("sessionUserRole");
        if ("MEDICO".equals(rol) && sessionId != null) {
            medicoId = sessionId;
        }
        logger.info("GET /medico/receta/nueva pacienteId={} medicoId={}", pacienteId, medicoId);
        model.addAttribute("pacienteId", pacienteId);
        model.addAttribute("medicoId", medicoId);
        return "nueva-receta";
    }
    
    
    @PostMapping("/receta/nueva")
    public String emitirReceta(@ModelAttribute CrearRecetaDTO dto, Model model) {
        logger.info("POST /medico/receta/nueva pacienteId={} medicamento={}", dto.getPacienteId(), dto.getMedicamento());
        try {
            RecetaDTO guardada = recetaFacade.crearReceta(dto);
            model.addAttribute("exito", true);
            model.addAttribute("receta", guardada);
            logger.info("Receta emitida con id={}", guardada.getId());
        } catch (Exception e) {
            logger.error("Error al emitir receta: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pacienteId", dto.getPacienteId());
            model.addAttribute("medicoId", dto.getMedicoId());
        }
        return "nueva-receta";
    }
    
    
    @GetMapping("/receta/ver")
    public String verRecetasPaciente(
            @RequestParam(name = "pacienteId") Long pacienteId,
            Model model) {
        logger.info("GET /medico/receta/ver pacienteId={}", pacienteId);
        try {
            List<RecetaDTO> recetas = recetaFacade.obtenerRecetasPaciente(pacienteId);
            model.addAttribute("recetas", recetas);
        } catch (Exception e) {
            logger.error("Error al obtener recetas: {}", e.getMessage());
            model.addAttribute("recetas", java.util.Collections.emptyList());
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("pacienteId", pacienteId);
        return "ver-recetas-paciente";
    }
}