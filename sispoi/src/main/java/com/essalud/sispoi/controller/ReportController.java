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
        
        try {
            byte[] reportData = reportService.generateReport_oc_r1(format, dependencyId, year, modification);
            
            HttpHeaders headers = new HttpHeaders();
            String filename = String.format("reporte_oc_r1_%d_%d_%d", dependencyId, year, modification);
            
            if ("excel".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentDispositionFormData("attachment", filename + ".xlsx");
            } else if ("pdf".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", filename + ".pdf");
            }
            
            return new ResponseEntity<>(reportData, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
