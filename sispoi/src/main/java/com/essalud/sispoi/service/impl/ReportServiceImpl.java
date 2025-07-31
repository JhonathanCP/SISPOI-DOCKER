package com.essalud.sispoi.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.essalud.sispoi.dto.OcR1ReportDTO;
import com.essalud.sispoi.model.Dependency;
import com.essalud.sispoi.repo.IDependencyRepo;
import com.essalud.sispoi.service.IOcR1Service;
import com.essalud.sispoi.service.IReportService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;

@Service
public class ReportServiceImpl implements IReportService{

    @Autowired
    private IOcR1Service ocR1Service;
    
    @Autowired
    private IDependencyRepo dependencyRepo;

    @Override
    public byte[] generateReport_oc_r1(String format, Integer dependencyId, Integer year, Integer modification) throws Exception {
        
        if ("excel".equalsIgnoreCase(format)) {
            return generateJasperExcelReport(dependencyId, year, modification);
        } else if ("pdf".equalsIgnoreCase(format)) {
            return generateJasperPdfReport(dependencyId, year, modification);
        } else if ("word".equalsIgnoreCase(format) || "docx".equalsIgnoreCase(format)) {
            return generateJasperWordReport(dependencyId, year, modification);
        }
        
        throw new IllegalArgumentException("Formato no soportado: " + format);
    }

    @Override
    public byte[] generateReport_oc_r2(String format, Integer dependencyId, Integer year, Integer modification) throws Exception {
        
        if ("excel".equalsIgnoreCase(format)) {
            return generateJasperExcelReport2(dependencyId, year, modification);
        } else if ("pdf".equalsIgnoreCase(format)) {
            return generateJasperPdfReport2(dependencyId, year, modification);
        } else if ("word".equalsIgnoreCase(format) || "docx".equalsIgnoreCase(format)) {
            return generateJasperWordReport2(dependencyId, year, modification);
        }
        
        throw new IllegalArgumentException("Formato no soportado: " + format);
    }

    @Override
    public List<OcR1ReportDTO> getOcR1Data(Integer dependencyId, Integer year, Integer modification) {
        return ocR1Service.getOcR1Data(dependencyId, year, modification);
    }

    // Método actualizado para usar la plantilla Jasper con datos reales
    private byte[] generateJasperPdfReport(Integer dependencyId, Integer year, Integer modification) throws Exception {
        System.out.println("=== INICIO generateJasperPdfReport ===");
        System.out.println("Parámetros: dependencyId=" + dependencyId + ", year=" + year + ", modification=" + modification);
        
        // Obtener los datos del procedimiento almacenado
        List<OcR1ReportDTO> data = getOcR1Data(dependencyId, year, modification);
        System.out.println("Datos obtenidos: " + data.size() + " registros");
        
        if (data.isEmpty()) {
            System.out.println("WARNING: No se encontraron datos para los parámetros especificados");
            // Crear un registro vacío para evitar errores en Jasper
            data = List.of(new OcR1ReportDTO());
        }
        
                // Convertir a formato compatible con JasperReports
        List<Map<String, Object>> jasperData = data.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            // Mapear campos según la plantilla Jasper (campos esperados por el .jrxml)
            map.put("oe", item.getCodOe()); // COD O.E.
            map.put("d_oe", item.getOe());  // Objetivo estratégico
            map.put("ae", item.getCodAe()); // COD A.E.
            map.put("d_ae", item.getAe());  // Acción estratégica
            map.put("cg", item.getCodCg()); // COD C.G.
            map.put("d_cg", item.getCg());  // Categoría genérica
            map.put("cc", item.getCodCc()); // COD C.C.
            map.put("d_cc", item.getCc());  // Centro de costo
            map.put("ff", item.getCodFf()); // COD F.F.
            map.put("d_ff", item.getFf());  // Fuente de financiamiento
            map.put("ao", item.getCodAo()); // COD A.O.
            map.put("d_ao", item.getAo());  // Actividad operativa
            map.put("mu", item.getMu());    // Meta
            // Convertir BigDecimal a Double para compatibilidad con Jasper
            map.put("i", item.getI() != null ? item.getI().doubleValue() : 0.0);      // I Trimestre
            map.put("ii", item.getIi() != null ? item.getIi().doubleValue() : 0.0);    // II Trimestre
            map.put("iii", item.getIii() != null ? item.getIii().doubleValue() : 0.0);  // III Trimestre
            map.put("iv", item.getIv() != null ? item.getIv().doubleValue() : 0.0);    // IV Trimestre
            map.put("tm", item.getTm() != null ? item.getTm().doubleValue() : 0.0);    // Total Meta
            map.put("rem", item.getRem() != null ? item.getRem().doubleValue() : 0.0);  // REM
            map.put("bie", item.getBie() != null ? item.getBie().doubleValue() : 0.0);  // BIE
            map.put("serv", item.getServ() != null ? item.getServ().doubleValue() : 0.0); // SERV
            map.put("tp", item.getTO() != null ? item.getTO().doubleValue() : 0.0);    // Total Presupuesto
            map.put("c_ao", item.getDa()); // Campo adicional que aparece en el jrxml
            return map;
        }).collect(Collectors.toList());

        System.out.println("Datos convertidos para Jasper: " + jasperData.size() + " registros");

        // Obtener el nombre de la dependencia
        Dependency dependency = dependencyRepo.findById(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependencia no encontrada: " + dependencyId));

        // Parámetros para la plantilla
        Map<String, Object> params = new HashMap<>();

        String etapa;
        switch (modification) {
            case 1:
                etapa = "Formulación Inicial";
                break;
            case 2:
                etapa = "Primera modificatoría";
                break;
            case 3:
                etapa = "Segunda modificatoría";
                break;
            case 4:
                etapa = "Tercera modificatoría";
                break;
            case 5:
                etapa = "Cuarta modificatoría";
                break;
            case 6:
                etapa = "Quinta modificatoría";
                break;
            case 7:
                etapa = "Sexta modificatoría";
                break;
            case 8:
                etapa = "Séptima modificatoría";
                break;
            default:
                etapa = "Formulación Inicial";
                break;
        }
        params.put("etapa", etapa);
        params.put("oc", dependency.getName());
        params.put("anio", year);        

        System.out.println("Parámetros Jasper: " + params);

        try {
            // Cargar el reporte Jasper desde plantilla simple
            JasperPrint jasperPrint;
            
            System.out.println("Compilando reporte desde oc_r1.jrxml...");
            
            // Usar la plantilla minimal
            InputStream simpleJrxmlStream = getClass().getResourceAsStream("/reports/oc_r1.jrxml");
            if (simpleJrxmlStream == null) {
                throw new RuntimeException("No se encontró el archivo oc_r1.jrxml");
            }
            System.out.println("Archivo oc_r1.jrxml encontrado exitosamente");
            
            // Verificar que podemos leer el contenido
            try {
                int available = simpleJrxmlStream.available();
                System.out.println("Bytes disponibles en el archivo: " + available);
            } catch (Exception e) {
                System.err.println("Error al leer información del archivo: " + e.getMessage());
            }
            
            try {
                System.out.println("Iniciando compilación del reporte...");
                JasperReport compiledReport = JasperCompileManager.compileReport(simpleJrxmlStream);
                System.out.println("Reporte compilado exitosamente");
                
                System.out.println("Iniciando llenado del reporte con datos...");
                jasperPrint = JasperFillManager.fillReport(
                    compiledReport, 
                    params, 
                    new JRBeanCollectionDataSource(jasperData)
                );
                System.out.println("Reporte llenado exitosamente");
                
            } catch (Exception compileException) {
                System.err.println("ERROR ESPECÍFICO en compilación: " + compileException.getClass().getSimpleName() + ": " + compileException.getMessage());
                compileException.printStackTrace();
                throw compileException;
            }
            
            System.out.println("Reporte compilado y llenado exitosamente desde oc_r1.jrxml");
            
            System.out.println("JasperPrint generado exitosamente");
            
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            System.out.println("PDF generado, tamaño: " + pdfBytes.length + " bytes");
            System.out.println("=== FIN generateJasperPdfReport ===");
            
            return pdfBytes;
            
        } catch (Exception e) {
            System.err.println("ERROR en generateJasperPdfReport: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // Método para generar reporte Excel
    private byte[] generateJasperExcelReport(Integer dependencyId, Integer year, Integer modification) throws Exception {
        System.out.println("=== INICIO generateJasperExcelReport ===");
        System.out.println("Parámetros: dependencyId=" + dependencyId + ", year=" + year + ", modification=" + modification);
        
        // Obtener datos
        List<OcR1ReportDTO> data = ocR1Service.getOcR1Data(dependencyId, year, modification);
        System.out.println("Datos obtenidos: " + data.size() + " registros");
        
        if (data.isEmpty()) {
            // Crear un registro vacío para evitar errores en Jasper
            data = List.of(new OcR1ReportDTO());
        }

        // Convertir a formato compatible con JasperReports
        List<Map<String, Object>> jasperData = data.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            // Mapear campos según la plantilla Jasper (campos esperados por el .jrxml)
            map.put("oe", item.getCodOe()); // COD O.E.
            map.put("d_oe", item.getOe());  // Objetivo estratégico
            map.put("ae", item.getCodAe()); // COD A.E.
            map.put("d_ae", item.getAe());  // Acción estratégica
            map.put("cg", item.getCodCg()); // COD C.G.
            map.put("d_cg", item.getCg());  // Categoría genérica
            map.put("cc", item.getCodCc()); // COD C.C.
            map.put("d_cc", item.getCc());  // Centro de costo
            map.put("ff", item.getCodFf()); // COD F.F.
            map.put("d_ff", item.getFf());  // Fuente de financiamiento
            map.put("ao", item.getCodAo()); // COD A.O.
            map.put("d_ao", item.getAo());  // Actividad operativa
            map.put("mu", item.getMu());    // Meta
            // Convertir BigDecimal a Double para compatibilidad con Jasper
            map.put("i", item.getI() != null ? item.getI().doubleValue() : 0.0);      // I Trimestre
            map.put("ii", item.getIi() != null ? item.getIi().doubleValue() : 0.0);    // II Trimestre
            map.put("iii", item.getIii() != null ? item.getIii().doubleValue() : 0.0);  // III Trimestre
            map.put("iv", item.getIv() != null ? item.getIv().doubleValue() : 0.0);    // IV Trimestre
            map.put("tm", item.getTm() != null ? item.getTm().doubleValue() : 0.0);    // Total Meta
            map.put("rem", item.getRem() != null ? item.getRem().doubleValue() : 0.0);  // REM
            map.put("bie", item.getBie() != null ? item.getBie().doubleValue() : 0.0);  // BIE
            map.put("serv", item.getServ() != null ? item.getServ().doubleValue() : 0.0); // SERV
            map.put("tp", item.getTO() != null ? item.getTO().doubleValue() : 0.0);    // Total Presupuesto
            map.put("c_ao", item.getDa()); // Campo adicional que aparece en el jrxml
            return map;
        }).collect(Collectors.toList());

        System.out.println("Datos convertidos para Jasper: " + jasperData.size() + " registros");

        // Obtener el nombre de la dependencia
        Dependency dependency = dependencyRepo.findById(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependencia no encontrada: " + dependencyId));

        // Parámetros para la plantilla
        Map<String, Object> params = new HashMap<>();
        String etapa;
        switch (modification) {
            case 1:
                etapa = "Formulación Inicial";
                break;
            case 2:
                etapa = "Primera modificatoría";
                break;
            case 3:
                etapa = "Segunda modificatoría";
                break;
            case 4:
                etapa = "Tercera modificatoría";
                break;
            case 5:
                etapa = "Cuarta modificatoría";
                break;
            case 6:
                etapa = "Quinta modificatoría";
                break;
            case 7:
                etapa = "Sexta modificatoría";
                break;
            case 8:
                etapa = "Séptima modificatoría";
                break;
            default:
                etapa = "Formulación Inicial";
                break;
        }
        params.put("etapa", etapa);
        params.put("oc", dependency.getName());
        params.put("anio", year);
        
        System.out.println("Parámetros Jasper: " + params);

        try {
            // Cargar el reporte Jasper desde plantilla simple
            JasperPrint jasperPrint;
            
            System.out.println("Compilando reporte Excel desde oc_r1.jrxml...");
            
            // Usar la plantilla minimal
            InputStream simpleJrxmlStream = getClass().getResourceAsStream("/reports/oc_r1.jrxml");
            if (simpleJrxmlStream == null) {
                throw new RuntimeException("No se encontró el archivo oc_r1.jrxml");
            }
            System.out.println("Archivo oc_r1.jrxml encontrado exitosamente para Excel");
            
            // Verificar que podemos leer el contenido del Excel
            try {
                int available = simpleJrxmlStream.available();
                System.out.println("Bytes disponibles en el archivo Excel: " + available);
            } catch (Exception e) {
                System.err.println("Error al leer información del archivo Excel: " + e.getMessage());
            }
            
            try {
                System.out.println("Iniciando compilación del reporte Excel...");
                JasperReport compiledReport = JasperCompileManager.compileReport(simpleJrxmlStream);
                System.out.println("Reporte Excel compilado exitosamente");
                
                System.out.println("Iniciando llenado del reporte Excel con datos...");
                jasperPrint = JasperFillManager.fillReport(
                    compiledReport, 
                    params, 
                    new JRBeanCollectionDataSource(jasperData)
                );
                System.out.println("Reporte Excel llenado exitosamente");
                
            } catch (Exception compileException) {
                System.err.println("ERROR ESPECÍFICO en compilación Excel: " + compileException.getClass().getSimpleName() + ": " + compileException.getMessage());
                compileException.printStackTrace();
                throw compileException;
            }
            
            System.out.println("Reporte compilado y llenado exitosamente desde oc_r1.jrxml");
            
            System.out.println("JasperPrint generado exitosamente");
            
            // Exportar a Excel
            ByteArrayOutputStream excelOutputStream = new ByteArrayOutputStream();
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(excelOutputStream));
            
            // Configuración para Excel
            SimpleXlsxReportConfiguration xlsxConfig = new SimpleXlsxReportConfiguration();
            xlsxConfig.setOnePagePerSheet(false);
            xlsxConfig.setDetectCellType(true);
            xlsxConfig.setCollapseRowSpan(false);
            exporter.setConfiguration(xlsxConfig);
            
            exporter.exportReport();
            
            byte[] excelBytes = excelOutputStream.toByteArray();
            System.out.println("Excel generado, tamaño: " + excelBytes.length + " bytes");
            System.out.println("=== FIN generateJasperExcelReport ===");
            
            return excelBytes;
            
        } catch (Exception e) {
            System.err.println("ERROR en generateJasperExcelReport: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Método para generar reporte Word para OC-R1
    private byte[] generateJasperWordReport(Integer dependencyId, Integer year, Integer modification) throws Exception {
        System.out.println("=== INICIO generateJasperWordReport ===");
        System.out.println("Parámetros: dependencyId=" + dependencyId + ", year=" + year + ", modification=" + modification);
        
        // Obtener datos
        List<OcR1ReportDTO> data = ocR1Service.getOcR1Data(dependencyId, year, modification);
        System.out.println("Datos obtenidos: " + data.size() + " registros");
        
        if (data.isEmpty()) {
            // Crear un registro vacío para evitar errores en Jasper
            data = List.of(new OcR1ReportDTO());
        }

        // Convertir a formato compatible con JasperReports
        List<Map<String, Object>> jasperData = data.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            // Mapear campos según la plantilla Jasper (campos esperados por el .jrxml)
            map.put("oe", item.getCodOe()); // COD O.E.
            map.put("d_oe", item.getOe());  // Objetivo estratégico
            map.put("ae", item.getCodAe()); // COD A.E.
            map.put("d_ae", item.getAe());  // Acción estratégica
            map.put("cg", item.getCodCg()); // COD C.G.
            map.put("d_cg", item.getCg());  // Categoría genérica
            map.put("cc", item.getCodCc()); // COD C.C.
            map.put("d_cc", item.getCc());  // Centro de costo
            map.put("ff", item.getCodFf()); // COD F.F.
            map.put("d_ff", item.getFf());  // Fuente de financiamiento
            map.put("ao", item.getCodAo()); // COD A.O.
            map.put("d_ao", item.getAo());  // Actividad operativa
            map.put("mu", item.getMu());    // Meta
            // Convertir BigDecimal a Double para compatibilidad con Jasper
            map.put("i", item.getI() != null ? item.getI().doubleValue() : 0.0);      // I Trimestre
            map.put("ii", item.getIi() != null ? item.getIi().doubleValue() : 0.0);    // II Trimestre
            map.put("iii", item.getIii() != null ? item.getIii().doubleValue() : 0.0);  // III Trimestre
            map.put("iv", item.getIv() != null ? item.getIv().doubleValue() : 0.0);    // IV Trimestre
            map.put("tm", item.getTm() != null ? item.getTm().doubleValue() : 0.0);    // Total Meta
            map.put("rem", item.getRem() != null ? item.getRem().doubleValue() : 0.0);  // REM
            map.put("bie", item.getBie() != null ? item.getBie().doubleValue() : 0.0);  // BIE
            map.put("serv", item.getServ() != null ? item.getServ().doubleValue() : 0.0); // SERV
            map.put("tp", item.getTO() != null ? item.getTO().doubleValue() : 0.0);    // Total Presupuesto
            map.put("c_ao", item.getDa()); // Campo adicional que aparece en el jrxml
            return map;
        }).collect(Collectors.toList());

        System.out.println("Datos convertidos para Jasper: " + jasperData.size() + " registros");

        // Obtener el nombre de la dependencia
        Dependency dependency = dependencyRepo.findById(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependencia no encontrada: " + dependencyId));

        // Parámetros para la plantilla
        Map<String, Object> params = new HashMap<>();
        String etapa;
        switch (modification) {
            case 1:
                etapa = "Formulación Inicial";
                break;
            case 2:
                etapa = "Primera modificatoría";
                break;
            case 3:
                etapa = "Segunda modificatoría";
                break;
            case 4:
                etapa = "Tercera modificatoría";
                break;
            case 5:
                etapa = "Cuarta modificatoría";
                break;
            case 6:
                etapa = "Quinta modificatoría";
                break;
            case 7:
                etapa = "Sexta modificatoría";
                break;
            case 8:
                etapa = "Séptima modificatoría";
                break;
            default:
                etapa = "Formulación Inicial";
                break;
        }
        params.put("etapa", etapa);
        params.put("oc", dependency.getName());
        params.put("anio", year);
        
        System.out.println("Parámetros Jasper: " + params);

        try {
            // Cargar el reporte Jasper desde plantilla simple
            JasperPrint jasperPrint;
            
            System.out.println("Compilando reporte Word desde oc_r1.jrxml...");
            
            // Usar la plantilla
            InputStream jrxmlStream = getClass().getResourceAsStream("/reports/oc_r1.jrxml");
            if (jrxmlStream == null) {
                throw new RuntimeException("No se encontró el archivo oc_r1.jrxml");
            }
            System.out.println("Archivo oc_r1.jrxml encontrado exitosamente para Word");
            
            // Verificar que podemos leer el contenido
            try {
                int available = jrxmlStream.available();
                System.out.println("Bytes disponibles en el archivo Word: " + available);
            } catch (Exception e) {
                System.err.println("Error al leer información del archivo Word: " + e.getMessage());
            }
            
            try {
                System.out.println("Iniciando compilación del reporte Word...");
                JasperReport compiledReport = JasperCompileManager.compileReport(jrxmlStream);
                System.out.println("Reporte Word compilado exitosamente");
                
                System.out.println("Iniciando llenado del reporte Word con datos...");
                jasperPrint = JasperFillManager.fillReport(
                    compiledReport, 
                    params, 
                    new JRBeanCollectionDataSource(jasperData)
                );
                System.out.println("Reporte Word llenado exitosamente");
                
            } catch (Exception compileException) {
                System.err.println("ERROR ESPECÍFICO en compilación Word: " + compileException.getClass().getSimpleName() + ": " + compileException.getMessage());
                compileException.printStackTrace();
                throw compileException;
            }
            
            System.out.println("Reporte compilado y llenado exitosamente desde oc_r1.jrxml");
            
            System.out.println("JasperPrint generado exitosamente");
            
            // Exportar a Word
            ByteArrayOutputStream wordOutputStream = new ByteArrayOutputStream();
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(wordOutputStream));
            
            // Configuración para Word
            SimpleDocxReportConfiguration docxConfig = new SimpleDocxReportConfiguration();
            docxConfig.setFlexibleRowHeight(true);
            exporter.setConfiguration(docxConfig);
            
            exporter.exportReport();
            
            byte[] wordBytes = wordOutputStream.toByteArray();
            System.out.println("Word generado, tamaño: " + wordBytes.length + " bytes");
            System.out.println("=== FIN generateJasperWordReport ===");
            
            return wordBytes;
            
        } catch (Exception e) {
            System.err.println("ERROR en generateJasperWordReport: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

        // Método actualizado para usar la plantilla Jasper con datos reales
    private byte[] generateJasperPdfReport2(Integer dependencyId, Integer year, Integer modification) throws Exception {
        System.out.println("=== INICIO generateJasperPdfReport ===");
        System.out.println("Parámetros: dependencyId=" + dependencyId + ", year=" + year + ", modification=" + modification);
        
        // Obtener los datos del procedimiento almacenado
        List<OcR1ReportDTO> data = getOcR1Data(dependencyId, year, modification);
        System.out.println("Datos obtenidos: " + data.size() + " registros");
        
        if (data.isEmpty()) {
            System.out.println("WARNING: No se encontraron datos para los parámetros especificados");
            // Crear un registro vacío para evitar errores en Jasper
            data = List.of(new OcR1ReportDTO());
        }
        
                // Convertir a formato compatible con JasperReports
        List<Map<String, Object>> jasperData = data.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            // Mapear campos según la plantilla Jasper (campos esperados por el .jrxml)
            map.put("oe", item.getCodOe()); // COD O.E.
            map.put("ae", item.getCodAe()); // COD A.E.
            map.put("ao", item.getCodAo()); // COD A.O.
            map.put("d_ao", item.getAo());  // Actividad operativa
            map.put("mu", item.getMu());    // Meta
            // Convertir BigDecimal a Double para compatibilidad con Jasper
            map.put("i", item.getI() != null ? item.getI().doubleValue() : 0.0);      // I Trimestre
            map.put("ii", item.getIi() != null ? item.getIi().doubleValue() : 0.0);    // II Trimestre
            map.put("iii", item.getIii() != null ? item.getIii().doubleValue() : 0.0);  // III Trimestre
            map.put("iv", item.getIv() != null ? item.getIv().doubleValue() : 0.0);    // IV Trimestre
            map.put("tm", item.getTm() != null ? item.getTm().doubleValue() : 0.0);    // Total Meta
            map.put("rem", item.getRem() != null ? item.getRem().doubleValue() : 0.0);  // REM
            map.put("bie", item.getBie() != null ? item.getBie().doubleValue() : 0.0);  // BIE
            map.put("serv", item.getServ() != null ? item.getServ().doubleValue() : 0.0); // SERV
            map.put("tp", item.getTO() != null ? item.getTO().doubleValue() : 0.0);    // Total Presupuesto
            map.put("c_ao", item.getDa()); // Campo adicional que aparece en el jrxml
            return map;
        }).collect(Collectors.toList());

        System.out.println("Datos convertidos para Jasper: " + jasperData.size() + " registros");

        // Obtener el nombre de la dependencia
        Dependency dependency = dependencyRepo.findById(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependencia no encontrada: " + dependencyId));

        // Parámetros para la plantilla
        Map<String, Object> params = new HashMap<>();
        String etapa;
        switch (modification) {
            case 1:
                etapa = "Formulación Inicial";
                break;
            case 2:
                etapa = "Primera modificatoría";
                break;
            case 3:
                etapa = "Segunda modificatoría";
                break;
            case 4:
                etapa = "Tercera modificatoría";
                break;
            case 5:
                etapa = "Cuarta modificatoría";
                break;
            case 6:
                etapa = "Quinta modificatoría";
                break;
            case 7:
                etapa = "Sexta modificatoría";
                break;
            case 8:
                etapa = "Séptima modificatoría";
                break;
            default:
                etapa = "Formulación Inicial";
                break;
        }
        params.put("etapa", etapa);
        params.put("oc", dependency.getName());
        params.put("anio", year);
        
        System.out.println("Parámetros Jasper: " + params);

        try {
            // Cargar el reporte Jasper desde plantilla simple
            JasperPrint jasperPrint;
            
            System.out.println("Compilando reporte desde oc_r2.jrxml...");
            
            // Usar la plantilla minimal
            InputStream simpleJrxmlStream = getClass().getResourceAsStream("/reports/oc_r2.jrxml");
            if (simpleJrxmlStream == null) {
                throw new RuntimeException("No se encontró el archivo oc_r2.jrxml");
            }
            System.out.println("Archivo oc_r2.jrxml encontrado exitosamente");
            
            // Verificar que podemos leer el contenido
            try {
                int available = simpleJrxmlStream.available();
                System.out.println("Bytes disponibles en el archivo: " + available);
            } catch (Exception e) {
                System.err.println("Error al leer información del archivo: " + e.getMessage());
            }
            
            try {
                System.out.println("Iniciando compilación del reporte...");
                JasperReport compiledReport = JasperCompileManager.compileReport(simpleJrxmlStream);
                System.out.println("Reporte compilado exitosamente");
                
                System.out.println("Iniciando llenado del reporte con datos...");
                jasperPrint = JasperFillManager.fillReport(
                    compiledReport, 
                    params, 
                    new JRBeanCollectionDataSource(jasperData)
                );
                System.out.println("Reporte llenado exitosamente");
                
            } catch (Exception compileException) {
                System.err.println("ERROR ESPECÍFICO en compilación: " + compileException.getClass().getSimpleName() + ": " + compileException.getMessage());
                compileException.printStackTrace();
                throw compileException;
            }
            
            System.out.println("Reporte compilado y llenado exitosamente desde oc_r2.jrxml");
            
            System.out.println("JasperPrint generado exitosamente");
            
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            System.out.println("PDF generado, tamaño: " + pdfBytes.length + " bytes");
            System.out.println("=== FIN generateJasperPdfReport ===");
            
            return pdfBytes;
            
        } catch (Exception e) {
            System.err.println("ERROR en generateJasperPdfReport: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // Método para generar reporte Excel
    private byte[] generateJasperExcelReport2(Integer dependencyId, Integer year, Integer modification) throws Exception {
        System.out.println("=== INICIO generateJasperExcelReport ===");
        System.out.println("Parámetros: dependencyId=" + dependencyId + ", year=" + year + ", modification=" + modification);
        
        // Obtener datos
        List<OcR1ReportDTO> data = ocR1Service.getOcR1Data(dependencyId, year, modification);
        System.out.println("Datos obtenidos: " + data.size() + " registros");
        
        if (data.isEmpty()) {
            // Crear un registro vacío para evitar errores en Jasper
            data = List.of(new OcR1ReportDTO());
        }

        // Convertir a formato compatible con JasperReports
        List<Map<String, Object>> jasperData = data.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            // Mapear campos según la plantilla Jasper (campos esperados por el .jrxml)
            map.put("oe", item.getCodOe()); // COD O.E.
            map.put("ae", item.getCodAe()); // COD A.E.
            map.put("ao", item.getCodAo()); // COD A.O.
            map.put("d_ao", item.getAo());  // Actividad operativa
            map.put("mu", item.getMu());    // Meta
            // Convertir BigDecimal a Double para compatibilidad con Jasper
            map.put("i", item.getI() != null ? item.getI().doubleValue() : 0.0);      // I Trimestre
            map.put("ii", item.getIi() != null ? item.getIi().doubleValue() : 0.0);    // II Trimestre
            map.put("iii", item.getIii() != null ? item.getIii().doubleValue() : 0.0);  // III Trimestre
            map.put("iv", item.getIv() != null ? item.getIv().doubleValue() : 0.0);    // IV Trimestre
            map.put("tm", item.getTm() != null ? item.getTm().doubleValue() : 0.0);    // Total Meta
            map.put("rem", item.getRem() != null ? item.getRem().doubleValue() : 0.0);  // REM
            map.put("bie", item.getBie() != null ? item.getBie().doubleValue() : 0.0);  // BIE
            map.put("serv", item.getServ() != null ? item.getServ().doubleValue() : 0.0); // SERV
            map.put("tp", item.getTO() != null ? item.getTO().doubleValue() : 0.0);    // Total Presupuesto
            map.put("c_ao", item.getDa()); // Campo adicional que aparece en el jrxml
            return map;
        }).collect(Collectors.toList());

        System.out.println("Datos convertidos para Jasper: " + jasperData.size() + " registros");

        // Obtener el nombre de la dependencia
        Dependency dependency = dependencyRepo.findById(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependencia no encontrada: " + dependencyId));

        // Parámetros para la plantilla
        Map<String, Object> params = new HashMap<>();
        String etapa;
        switch (modification) {
            case 1:
                etapa = "Formulación Inicial";
                break;
            case 2:
                etapa = "Primera modificatoría";
                break;
            case 3:
                etapa = "Segunda modificatoría";
                break;
            case 4:
                etapa = "Tercera modificatoría";
                break;
            case 5:
                etapa = "Cuarta modificatoría";
                break;
            case 6:
                etapa = "Quinta modificatoría";
                break;
            case 7:
                etapa = "Sexta modificatoría";
                break;
            case 8:
                etapa = "Séptima modificatoría";
                break;
            default:
                etapa = "Formulación Inicial";
                break;
        }
        params.put("etapa", etapa);
        params.put("oc", dependency.getName());
        params.put("anio", year);
        
        System.out.println("Parámetros Jasper: " + params);

        try {
            // Cargar el reporte Jasper desde plantilla simple
            JasperPrint jasperPrint;
            
            System.out.println("Compilando reporte Excel desde oc_r2.jrxml...");
            
            // Usar la plantilla minimal
            InputStream simpleJrxmlStream = getClass().getResourceAsStream("/reports/oc_r2.jrxml");
            if (simpleJrxmlStream == null) {
                throw new RuntimeException("No se encontró el archivo oc_r2.jrxml");
            }
            System.out.println("Archivo oc_r2.jrxml encontrado exitosamente para Excel");
            
            // Verificar que podemos leer el contenido del Excel
            try {
                int available = simpleJrxmlStream.available();
                System.out.println("Bytes disponibles en el archivo Excel: " + available);
            } catch (Exception e) {
                System.err.println("Error al leer información del archivo Excel: " + e.getMessage());
            }
            
            try {
                System.out.println("Iniciando compilación del reporte Excel...");
                JasperReport compiledReport = JasperCompileManager.compileReport(simpleJrxmlStream);
                System.out.println("Reporte Excel compilado exitosamente");
                
                System.out.println("Iniciando llenado del reporte Excel con datos...");
                jasperPrint = JasperFillManager.fillReport(
                    compiledReport, 
                    params, 
                    new JRBeanCollectionDataSource(jasperData)
                );
                System.out.println("Reporte Excel llenado exitosamente");
                
            } catch (Exception compileException) {
                System.err.println("ERROR ESPECÍFICO en compilación Excel: " + compileException.getClass().getSimpleName() + ": " + compileException.getMessage());
                compileException.printStackTrace();
                throw compileException;
            }
            
            System.out.println("Reporte compilado y llenado exitosamente desde oc_r2.jrxml");
            
            System.out.println("JasperPrint generado exitosamente");
            
            // Exportar a Excel
            ByteArrayOutputStream excelOutputStream = new ByteArrayOutputStream();
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(excelOutputStream));
            
            // Configuración para Excel
            SimpleXlsxReportConfiguration xlsxConfig = new SimpleXlsxReportConfiguration();
            xlsxConfig.setOnePagePerSheet(false);
            xlsxConfig.setDetectCellType(true);
            xlsxConfig.setCollapseRowSpan(false);
            exporter.setConfiguration(xlsxConfig);
            
            exporter.exportReport();
            
            byte[] excelBytes = excelOutputStream.toByteArray();
            System.out.println("Excel generado, tamaño: " + excelBytes.length + " bytes");
            System.out.println("=== FIN generateJasperExcelReport ===");
            
            return excelBytes;
            
        } catch (Exception e) {
            System.err.println("ERROR en generateJasperExcelReport: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Método para generar reporte Word para OC-R2
    private byte[] generateJasperWordReport2(Integer dependencyId, Integer year, Integer modification) throws Exception {
        System.out.println("=== INICIO generateJasperWordReport2 ===");
        System.out.println("Parámetros: dependencyId=" + dependencyId + ", year=" + year + ", modification=" + modification);
        
        // Obtener los datos del procedimiento almacenado (usando OC-R1 por ahora)
        List<OcR1ReportDTO> data = ocR1Service.getOcR1Data(dependencyId, year, modification);
        System.out.println("Datos obtenidos: " + data.size() + " registros");
        
        if (data.isEmpty()) {
            // Crear un registro vacío para evitar errores en Jasper
            data = List.of(new OcR1ReportDTO());
        }

        // Convertir a formato compatible con JasperReports para OC-R2
        List<Map<String, Object>> jasperData = data.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            // Mapear campos según la plantilla OC-R2 (campos esperados por el .jrxml)
            map.put("oe", item.getCodOe()); // COD O.E.
            map.put("ae", item.getCodAe()); // COD A.E.
            map.put("ao", item.getCodAo()); // COD A.O.
            map.put("d_ao", item.getAo());  // Actividad operativa
            map.put("mu", item.getMu());    // Meta
            // Convertir BigDecimal a Double para compatibilidad con Jasper
            map.put("i", item.getI() != null ? item.getI().doubleValue() : 0.0);      // I Trimestre
            map.put("ii", item.getIi() != null ? item.getIi().doubleValue() : 0.0);    // II Trimestre
            map.put("iii", item.getIii() != null ? item.getIii().doubleValue() : 0.0);  // III Trimestre
            map.put("iv", item.getIv() != null ? item.getIv().doubleValue() : 0.0);    // IV Trimestre
            map.put("tm", item.getTm() != null ? item.getTm().doubleValue() : 0.0);    // Total Meta
            map.put("rem", item.getRem() != null ? item.getRem().doubleValue() : 0.0);  // REM
            map.put("bie", item.getBie() != null ? item.getBie().doubleValue() : 0.0);  // BIE
            map.put("serv", item.getServ() != null ? item.getServ().doubleValue() : 0.0); // SERV
            map.put("tp", item.getTO() != null ? item.getTO().doubleValue() : 0.0);    // Total Presupuesto
            map.put("c_ao", item.getDa()); // Campo adicional que aparece en el jrxml
            return map;
        }).collect(Collectors.toList());

        System.out.println("Datos convertidos para Jasper: " + jasperData.size() + " registros");

        // Obtener el nombre de la dependencia
        Dependency dependency = dependencyRepo.findById(dependencyId)
                .orElseThrow(() -> new IllegalArgumentException("Dependencia no encontrada: " + dependencyId));

        // Parámetros para la plantilla
        Map<String, Object> params = new HashMap<>();
        String etapa;
        switch (modification) {
            case 1:
                etapa = "Formulación Inicial";
                break;
            case 2:
                etapa = "Primera modificatoría";
                break;
            case 3:
                etapa = "Segunda modificatoría";
                break;
            case 4:
                etapa = "Tercera modificatoría";
                break;
            case 5:
                etapa = "Cuarta modificatoría";
                break;
            case 6:
                etapa = "Quinta modificatoría";
                break;
            case 7:
                etapa = "Sexta modificatoría";
                break;
            case 8:
                etapa = "Séptima modificatoría";
                break;
            default:
                etapa = "Formulación Inicial";
                break;
        }
        params.put("etapa", etapa);
        params.put("oc", dependency.getName());
        params.put("anio", year);
        
        System.out.println("Parámetros Jasper: " + params);

        try {
            // Cargar el reporte Jasper desde plantilla simple
            JasperPrint jasperPrint;
            
            System.out.println("Compilando reporte Word desde oc_r2.jrxml...");
            
            // Usar la plantilla limpia
            InputStream jrxmlStream = getClass().getResourceAsStream("/reports/oc_r2.jrxml");
            if (jrxmlStream == null) {
                throw new RuntimeException("No se encontró el archivo oc_r2.jrxml");
            }
            System.out.println("Archivo oc_r2.jrxml encontrado exitosamente para Word");
            
            // Verificar que podemos leer el contenido
            try {
                int available = jrxmlStream.available();
                System.out.println("Bytes disponibles en el archivo Word: " + available);
            } catch (Exception e) {
                System.err.println("Error al leer información del archivo Word: " + e.getMessage());
            }
            
            try {
                System.out.println("Iniciando compilación del reporte Word...");
                JasperReport compiledReport = JasperCompileManager.compileReport(jrxmlStream);
                System.out.println("Reporte Word compilado exitosamente");
                
                System.out.println("Iniciando llenado del reporte Word con datos...");
                jasperPrint = JasperFillManager.fillReport(
                    compiledReport, 
                    params, 
                    new JRBeanCollectionDataSource(jasperData)
                );
                System.out.println("Reporte Word llenado exitosamente");
                
            } catch (Exception compileException) {
                System.err.println("ERROR ESPECÍFICO en compilación Word: " + compileException.getClass().getSimpleName() + ": " + compileException.getMessage());
                compileException.printStackTrace();
                throw compileException;
            }
            
            System.out.println("Reporte compilado y llenado exitosamente desde oc_r2.jrxml");
            
            System.out.println("JasperPrint generado exitosamente");
            
            // Exportar a Word
            ByteArrayOutputStream wordOutputStream = new ByteArrayOutputStream();
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(wordOutputStream));
            
            // Configuración para Word
            SimpleDocxReportConfiguration docxConfig = new SimpleDocxReportConfiguration();
            docxConfig.setFlexibleRowHeight(true);
            exporter.setConfiguration(docxConfig);
            
            exporter.exportReport();
            
            byte[] wordBytes = wordOutputStream.toByteArray();
            System.out.println("Word generado, tamaño: " + wordBytes.length + " bytes");
            System.out.println("=== FIN generateJasperWordReport2 ===");
            
            return wordBytes;
            
        } catch (Exception e) {
            System.err.println("ERROR en generateJasperWordReport2: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}