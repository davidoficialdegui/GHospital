package com.gestionHospitalaria.controller;
 
import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import com.gestionHospitalaria.facade.DiagnosticoFacade;
import com.gestionHospitalaria.facade.PacienteFacade;
import com.gestionHospitalaria.service.InformeMedicoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
import java.util.List;
 
/**
 * Controlador de las vistas del PACIENTE.
 *
 * Cubre dos historias de usuario:
 *   - Como paciente quiero consultar mi historial médico.
 *   - Como paciente quiero descargar mis informes médicos en PDF.
 */
@Controller
@RequestMapping("/paciente")
public class PacienteViewController {
 
    private static final Logger logger = LoggerFactory.getLogger(PacienteViewController.class);
 
    @Autowired
    private PacienteFacade pacienteFacade;
 
    @Autowired
    private DiagnosticoFacade diagnosticoFacade;
 
    @Autowired
    private InformeMedicoService informeMedicoService;
 
    /**
     * GET /paciente/historial?pacienteId=1
     * Muestra los datos clínicos del paciente y sus diagnósticos previos.
     */
    @GetMapping("/historial")
    public String verMiHistorial(
            @RequestParam(name = "pacienteId", required = false) Long pacienteId,
            HttpSession session,
            Model model) {

        logger.info("GET /paciente/historial pacienteId={}", pacienteId);

        // Control de acceso: un PACIENTE solo puede ver su propio historial
        String rol        = (String) session.getAttribute("sessionUserRole");
        Long   sessionId  = (Long)   session.getAttribute("sessionUserId");

        if ("PACIENTE".equals(rol) && sessionId != null) {
            // Ignorar el parámetro de la URL y forzar su propio ID
            pacienteId = sessionId;
        }

        if (pacienteId != null) {
            try {
                HistorialMedicoDTO historial = pacienteFacade.obtenerHistorial(pacienteId);
                List<DiagnosticoDTO> diagnosticos =
                        diagnosticoFacade.obtenerDiagnosticosPaciente(pacienteId);

                model.addAttribute("historial", historial);
                model.addAttribute("diagnosticos", diagnosticos);
                model.addAttribute("pacienteId", pacienteId);
            } catch (Exception e) {
                logger.error("Error obteniendo historial del paciente {}: {}",
                        pacienteId, e.getMessage());
                model.addAttribute("error", e.getMessage());
                model.addAttribute("pacienteId", pacienteId);
            }
        }

        return "mi-historial";
    }
 
    /**
     * GET /paciente/historial/descargar?pacienteId=1
     * Devuelve el informe médico completo del paciente como PDF descargable.
     */
    @GetMapping("/historial/descargar")
    public ResponseEntity<byte[]> descargarInforme(
            @RequestParam("pacienteId") Long pacienteId,
            HttpSession session) {

        // Control de acceso: paciente solo puede descargar su propio informe
        String rol       = (String) session.getAttribute("sessionUserRole");
        Long   sessionId = (Long)   session.getAttribute("sessionUserId");
        if ("PACIENTE".equals(rol) && sessionId != null) {
            pacienteId = sessionId;
        }

        logger.info("GET /paciente/historial/descargar pacienteId={}", pacienteId);
 
        HistorialMedicoDTO historial = pacienteFacade.obtenerHistorial(pacienteId);
        List<DiagnosticoDTO> diagnosticos =
                diagnosticoFacade.obtenerDiagnosticosPaciente(pacienteId);
 
        byte[] pdf = informeMedicoService.generarInformePdf(historial, diagnosticos);
 
        String nombreArchivo = "informe_medico_" + pacienteId + ".pdf";
 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename(nombreArchivo).build());
        headers.setContentLength(pdf.length);
 
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}