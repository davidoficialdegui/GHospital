package com.gestionHospitalaria.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception e, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error-detalle");

        StringBuilder sb = new StringBuilder();
        sb.append("<h2 style='color:red'>Error: ").append(e.getClass().getSimpleName()).append("</h2>");
        sb.append("<p><strong>Mensaje:</strong> ").append(e.getMessage()).append("</p>");
        sb.append("<p><strong>URL:</strong> ").append(request.getRequestURL()).append("</p>");
        if (e.getCause() != null) {
            sb.append("<p><strong>Causa:</strong> ").append(e.getCause().getMessage()).append("</p>");
        }
        sb.append("<pre style='background:#f5f5f5;padding:10px'>");
        for (StackTraceElement el : e.getStackTrace()) {
            if (el.getClassName().contains("gestionHospitalaria")) {
                sb.append(">>> ").append(el.toString()).append("\n");
            }
        }
        sb.append("</pre>");

        mav.addObject("errorHtml", sb.toString());
        return mav;
    }
}