package com.essalud.sispoi.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.essalud.sispoi.dto.OcR1ReportDTO;
import com.essalud.sispoi.service.IOcR1Service;
import com.essalud.sispoi.service.IReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceImpl implements IReportService{

    @Autowired
    private IOcR1Service ocR1Service;

    @Override
    public byte[] generateReport_oc_r1(String format, Integer dependencyId, Integer year, Integer modification) throws Exception {
        List<OcR1ReportDTO> data = getOcR1Data(dependencyId, year, modification);
        
        if ("excel".equalsIgnoreCase(format)) {
            return generateExcelReport(data);
        } else if ("pdf".equalsIgnoreCase(format)) {
            return generatePdfReport(dependencyId, year, modification);
        }
        
        throw new IllegalArgumentException("Formato no soportado: " + format);
    }

    @Override
    public List<OcR1ReportDTO> getOcR1Data(Integer dependencyId, Integer year, Integer modification) {
        return ocR1Service.getOcR1Data(dependencyId, year, modification);
    }

    // Método actualizado para usar la plantilla Jasper con datos reales
    private byte[] generatePdfReport(Integer dependencyId, Integer year, Integer modification) throws Exception {
        // Obtener los datos del procedimiento almacenado
        List<OcR1ReportDTO> data = getOcR1Data(dependencyId, year, modification);
        
        // Convertir a formato compatible con JasperReports
        List<Map<String, Object>> jasperData = data.stream().map(item -> {
            Map<String, Object> row = new HashMap<>();
            // Mapear campos según la plantilla Jasper
            row.put("d_oe", item.getOe());                    // Objetivo estratégico
            row.put("d_ae", item.getAe());                    // Acción estratégica  
            row.put("d_cg", item.getCg());                    // Centro gestor
            row.put("d_cc", item.getCc());                    // Centro de costos
            row.put("d_ff", item.getFf());                    // Fondo financiero
            row.put("d_ao", item.getAo());                    // Actividad operativa
            row.put("c_ao", item.getDa());                    // Detalle de actividad
            row.put("mu", item.getMu());                      // Unidad de medida
            row.put("i", item.getI());                        // Meta trimestre I
            row.put("ii", item.getIi());                      // Meta trimestre II
            row.put("iii", item.getIii());                    // Meta trimestre III
            row.put("iv", item.getIv());                      // Meta trimestre IV
            row.put("rem", item.getRem());                    // Remuneración
            row.put("bie", item.getBie());                    // Bienes
            row.put("serv", item.getServ());                  // Servicios
            row.put("total", item.getTO());                   // Total
            return row;
        }).collect(Collectors.toList());

        // Parámetros para la plantilla
        Map<String, Object> params = new HashMap<>();
        params.put("oc", "Reporte OC-R1");                   // Título del reporte
        params.put("anio", year);                            // Año del reporte

        // Generar el reporte PDF
        File file = new ClassPathResource("/reports/oc_r1.jasper").getFile();
        JasperPrint print = JasperFillManager.fillReport(
            file.getPath(), 
            params, 
            new JRBeanCollectionDataSource(jasperData)
        );
        
        return JasperExportManager.exportReportToPdf(print);
    }

    private byte[] generateExcelReport(List<OcR1ReportDTO> data) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte OC-R1");

        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "Cód. OE", "OE", "Cód. AE", "AE", "Cód. CG", "CG", "Cód. CC", "CC",
            "Cód. FF", "FF", "Cód. AO", "AO", "MU", "I", "II", "III", "IV", "TM",
            "Remuneración", "Bienes", "Servicios", "Total", "Descripción"
        };

        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Llenar datos
        int rowNum = 1;
        for (OcR1ReportDTO item : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getCodOe());
            row.createCell(1).setCellValue(item.getOe());
            row.createCell(2).setCellValue(item.getCodAe());
            row.createCell(3).setCellValue(item.getAe());
            row.createCell(4).setCellValue(item.getCodCg());
            row.createCell(5).setCellValue(item.getCg());
            row.createCell(6).setCellValue(item.getCodCc());
            row.createCell(7).setCellValue(item.getCc());
            row.createCell(8).setCellValue(item.getCodFf());
            row.createCell(9).setCellValue(item.getFf());
            row.createCell(10).setCellValue(item.getCodAo());
            row.createCell(11).setCellValue(item.getAo());
            row.createCell(12).setCellValue(item.getMu());
            row.createCell(13).setCellValue(item.getI() != null ? item.getI().doubleValue() : 0);
            row.createCell(14).setCellValue(item.getIi() != null ? item.getIi().doubleValue() : 0);
            row.createCell(15).setCellValue(item.getIii() != null ? item.getIii().doubleValue() : 0);
            row.createCell(16).setCellValue(item.getIv() != null ? item.getIv().doubleValue() : 0);
            row.createCell(17).setCellValue(item.getTm() != null ? item.getTm().doubleValue() : 0);
            row.createCell(18).setCellValue(item.getRem() != null ? item.getRem().doubleValue() : 0);
            row.createCell(19).setCellValue(item.getBie() != null ? item.getBie().doubleValue() : 0);
            row.createCell(20).setCellValue(item.getServ() != null ? item.getServ().doubleValue() : 0);
            row.createCell(21).setCellValue(item.getTO() != null ? item.getTO().doubleValue() : 0);
            row.createCell(22).setCellValue(item.getDa());
        }

        // Auto-ajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}