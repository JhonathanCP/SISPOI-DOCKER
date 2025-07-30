package com.essalud.sispoi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.essalud.sispoi.dto.OcR1ReportDTO;
import com.essalud.sispoi.service.IReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private IReportService reportService;

    @GetMapping("/oc-r1")
    public ResponseEntity<List<OcR1ReportDTO>> getOcR1Data(
            @RequestParam Integer dependencyId,
            @RequestParam Integer year,
            @RequestParam Integer modification) {
        
        List<OcR1ReportDTO> data = reportService.getOcR1Data(dependencyId, year, modification);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/oc-r1/download")
    public ResponseEntity<byte[]> downloadOcR1Report(
            @RequestParam Integer dependencyId,
            @RequestParam Integer year,
            @RequestParam Integer modification,
            @RequestParam(defaultValue = "excel") String format) {
        
        System.out.println("=== INICIO downloadOcR1Report ===");
        System.out.println("Parámetros recibidos: dependencyId=" + dependencyId + ", year=" + year + ", modification=" + modification + ", format=" + format);
        
        try {
            byte[] reportData = reportService.generateReport_oc_r1(format, dependencyId, year, modification);
            
            if (reportData == null || reportData.length == 0) {
                System.err.println("ERROR: reportData está vacío o es null");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            System.out.println("Reporte generado exitosamente, tamaño: " + reportData.length + " bytes");
            
            HttpHeaders headers = new HttpHeaders();
            String filename = String.format("reporte_oc_r1_%d_%d_%d", dependencyId, year, modification);
            
            if ("excel".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentDispositionFormData("attachment", filename + ".xlsx");
                System.out.println("Configurado para descarga Excel: " + filename + ".xlsx");
            } else if ("pdf".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", filename + ".pdf");
                System.out.println("Configurado para descarga PDF: " + filename + ".pdf");
            } else if ("word".equalsIgnoreCase(format) || "docx".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
                headers.setContentDispositionFormData("attachment", filename + ".docx");
                System.out.println("Configurado para descarga Word: " + filename + ".docx");
            }
            
            System.out.println("Headers configurados: " + headers);
            System.out.println("=== FIN downloadOcR1Report ===");
            
            return new ResponseEntity<>(reportData, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            System.err.println("ERROR en downloadOcR1Report: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/oc-r2/download")
    public ResponseEntity<byte[]> downloadOcR2Report(
            @RequestParam Integer dependencyId,
            @RequestParam Integer year,
            @RequestParam Integer modification,
            @RequestParam(defaultValue = "excel") String format) {

        System.out.println("=== INICIO downloadOcR2Report ===");
        System.out.println("Parámetros recibidos: dependencyId=" + dependencyId + ", year=" + year + ", modification=" + modification + ", format=" + format);
        
        try {
            byte[] reportData = reportService.generateReport_oc_r2(format, dependencyId, year, modification);
            
            if (reportData == null || reportData.length == 0) {
                System.err.println("ERROR: reportData está vacío o es null");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            System.out.println("Reporte generado exitosamente, tamaño: " + reportData.length + " bytes");
            
            HttpHeaders headers = new HttpHeaders();
            String filename = String.format("reporte_oc_r2_%d_%d_%d", dependencyId, year, modification);
            
            if ("excel".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentDispositionFormData("attachment", filename + ".xlsx");
                System.out.println("Configurado para descarga Excel: " + filename + ".xlsx");
            } else if ("pdf".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", filename + ".pdf");
                System.out.println("Configurado para descarga PDF: " + filename + ".pdf");
            } else if ("word".equalsIgnoreCase(format) || "docx".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
                headers.setContentDispositionFormData("attachment", filename + ".docx");
                System.out.println("Configurado para descarga Word: " + filename + ".docx");
            }
            
            System.out.println("Headers configurados: " + headers);
            System.out.println("=== FIN downloadOcR2Report ===");
            
            return new ResponseEntity<>(reportData, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            System.err.println("ERROR en downloadOcR2Report: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
