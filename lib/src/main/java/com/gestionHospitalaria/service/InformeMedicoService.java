package com.gestionHospitalaria.service;

import com.gestionHospitalaria.dto.DiagnosticoDTO;
import com.gestionHospitalaria.dto.HistorialMedicoDTO;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio que construye el informe médico de un paciente como PDF (OpenPDF).
 *
 * El informe incluye:
 *   - Cabecera con fecha de generación.
 *   - Datos personales y vitales del paciente.
 *   - Información clínica (alergias, medicación, antecedentes...).
 *   - Listado de diagnósticos y tratamientos.
 */
@Service
public class InformeMedicoService {

    private static final DateTimeFormatter FECHA_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final Color AZUL_CABECERA = new Color(0, 123, 255);
    private static final Color GRIS_CLARO    = new Color(248, 249, 250);
    private static final Color GRIS_DIAG     = new Color(240, 240, 240);

    public byte[] generarInformePdf(HistorialMedicoDTO historial,
                                    List<DiagnosticoDTO> diagnosticos) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            Font fontTitulo    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, AZUL_CABECERA);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, new Color(50, 50, 50));
            Font fontEtiqueta  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font fontTexto     = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font fontPequeno   = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, Color.GRAY);

            // ── Cabecera ─────────────────────────────────────────────
            Paragraph titulo = new Paragraph("INFORME MÉDICO", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            doc.add(titulo);

            Paragraph fechaGen = new Paragraph(
                    "Generado el " + LocalDateTime.now().format(FECHA_FMT), fontPequeno);
            fechaGen.setAlignment(Element.ALIGN_CENTER);
            fechaGen.setSpacingAfter(20);
            doc.add(fechaGen);

            // ── Datos del paciente ───────────────────────────────────
            doc.add(new Paragraph("DATOS DEL PACIENTE", fontSubtitulo));
            doc.add(new Paragraph(" "));

            PdfPTable tablaDatos = new PdfPTable(2);
            tablaDatos.setWidthPercentage(100);
            tablaDatos.setWidths(new float[]{1f, 2f});

            agregarFila(tablaDatos, "Nombre completo", valor(historial.getNombreCompleto()),
                    fontEtiqueta, fontTexto);
            agregarFila(tablaDatos, "Grupo sanguíneo", valor(historial.getGrupoSanguineo()),
                    fontEtiqueta, fontTexto);
            agregarFila(tablaDatos, "Altura",
                    historial.getAltura() != null ? historial.getAltura() + " m" : "—",
                    fontEtiqueta, fontTexto);
            agregarFila(tablaDatos, "Peso",
                    historial.getPeso() != null ? historial.getPeso() + " kg" : "—",
                    fontEtiqueta, fontTexto);

            doc.add(tablaDatos);
            doc.add(new Paragraph(" "));

            // ── Información clínica ──────────────────────────────────
            doc.add(new Paragraph("INFORMACIÓN CLÍNICA", fontSubtitulo));
            doc.add(new Paragraph(" "));

            PdfPTable tablaClin = new PdfPTable(2);
            tablaClin.setWidthPercentage(100);
            tablaClin.setWidths(new float[]{1f, 2f});

            agregarFila(tablaClin, "Alergias", valor(historial.getAlergias()), fontEtiqueta, fontTexto);
            agregarFila(tablaClin, "Medicación actual", valor(historial.getMedicacionActual()), fontEtiqueta, fontTexto);
            agregarFila(tablaClin, "Enfermedades previas", valor(historial.getEnfermedadesPrevias()), fontEtiqueta, fontTexto);
            agregarFila(tablaClin, "Antecedentes familiares", valor(historial.getAntecedentesFamiliares()), fontEtiqueta, fontTexto);
            agregarFila(tablaClin, "Observaciones", valor(historial.getObservacionesMedicas()), fontEtiqueta, fontTexto);

            doc.add(tablaClin);
            doc.add(new Paragraph(" "));

            // ── Diagnósticos ─────────────────────────────────────────
            doc.add(new Paragraph("DIAGNÓSTICOS Y TRATAMIENTOS", fontSubtitulo));
            doc.add(new Paragraph(" "));

            if (diagnosticos == null || diagnosticos.isEmpty()) {
                doc.add(new Paragraph("No hay diagnósticos registrados.", fontTexto));
            } else {
                for (DiagnosticoDTO d : diagnosticos) {
                    PdfPTable tDiag = new PdfPTable(1);
                    tDiag.setWidthPercentage(100);
                    tDiag.setSpacingBefore(8);

                    String fecha = d.getFechaDiagnostico() != null
                            ? d.getFechaDiagnostico().format(FECHA_FMT) : "—";

                    PdfPCell cab = new PdfPCell(new Phrase(
                            fecha + "   |   Dr/a. " + valor(d.getMedicoNombre()), fontEtiqueta));
                    cab.setBackgroundColor(GRIS_DIAG);
                    cab.setPadding(8);
                    tDiag.addCell(cab);

                    PdfPCell cuerpo = new PdfPCell();
                    cuerpo.setPadding(8);
                    cuerpo.addElement(new Paragraph("Descripción: " + valor(d.getDescripcion()), fontTexto));
                    if (d.getTratamiento() != null && !d.getTratamiento().isBlank()) {
                        cuerpo.addElement(new Paragraph("Tratamiento: " + d.getTratamiento(), fontTexto));
                    }
                    if (d.getObservaciones() != null && !d.getObservaciones().isBlank()) {
                        cuerpo.addElement(new Paragraph("Observaciones: " + d.getObservaciones(), fontTexto));
                    }
                    tDiag.addCell(cuerpo);

                    doc.add(tDiag);
                }
            }

            // ── Pie ──────────────────────────────────────────────────
            Paragraph pie = new Paragraph(
                    "\nDocumento generado automáticamente por el Sistema de Gestión Hospitalaria.",
                    fontPequeno);
            pie.setAlignment(Element.ALIGN_CENTER);
            doc.add(pie);

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando el informe PDF: " + e.getMessage(), e);
        }
    }

    private void agregarFila(PdfPTable tabla, String etiqueta, String valor,
                             Font fEtq, Font fTxt) {
        PdfPCell celdaEtq = new PdfPCell(new Phrase(etiqueta, fEtq));
        celdaEtq.setPadding(6);
        celdaEtq.setBackgroundColor(GRIS_CLARO);
        tabla.addCell(celdaEtq);

        PdfPCell celdaVal = new PdfPCell(new Phrase(valor, fTxt));
        celdaVal.setPadding(6);
        tabla.addCell(celdaVal);
    }

    private String valor(String s) {
        return (s == null || s.isBlank()) ? "—" : s;
    }
}