package com.essalud.sispoi.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
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
import com.essalud.sispoi.repo.IOcR1ReportRepo;
import com.essalud.sispoi.service.IReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceImpl implements IReportService{

    @Autowired
    private IOcR1ReportRepo ocR1ReportRepo;

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
        List<Object[]> results = ocR1ReportRepo.getOcR1Report(dependencyId, year, modification);
        
        return results.stream().map(row -> {
            OcR1ReportDTO dto = new OcR1ReportDTO();
            dto.setCodOe(getString(row[0]));
            dto.setOe(getString(row[1]));
            dto.setCodAe(getString(row[2]));
            dto.setAe(getString(row[3]));
            dto.setCodCg(getString(row[4]));
            dto.setCg(getString(row[5]));
            dto.setCodCc(getString(row[6]));
            dto.setCc(getString(row[7]));
            dto.setCodFf(getString(row[8]));
            dto.setFf(getString(row[9]));
            dto.setCodAo(getString(row[10]));
            dto.setAo(getString(row[11]));
            dto.setMu(getString(row[12]));
            dto.setI(getBigDecimal(row[13]));
            dto.setIi(getBigDecimal(row[14]));
            dto.setIii(getBigDecimal(row[15]));
            dto.setIv(getBigDecimal(row[16]));
            dto.setTm(getBigDecimal(row[17]));
            dto.setRem(getBigDecimal(row[18]));
            dto.setBie(getBigDecimal(row[19]));
            dto.setServ(getBigDecimal(row[20]));
            dto.setTO(getBigDecimal(row[21]));
            dto.setDa(getString(row[22]));
            return dto;
        }).collect(Collectors.toList());
    }

    // Método legacy para PDF con JasperReports
    private byte[] generatePdfReport(Integer dependencyId, Integer year, Integer modification) throws Exception {
        byte[] data = null;

        Map<String, Object> params = new HashMap<>();
        params.put("oc", "TEST");
        params.put("anio", year);

        File file = new ClassPathResource("/reports/oc_r1.jasper").getFile();
        JasperPrint print = JasperFillManager.fillReport(file.getPath(), params, new JRBeanCollectionDataSource(null));
        data = JasperExportManager.exportReportToPdf(print);

        return data;
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

    private String getString(Object value) {
        return value != null ? value.toString() : "";
    }

    private BigDecimal getBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        return BigDecimal.ZERO;
    }
}