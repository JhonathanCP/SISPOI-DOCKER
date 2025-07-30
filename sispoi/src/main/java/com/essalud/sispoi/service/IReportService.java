package com.essalud.sispoi.service;

import java.util.List;

import com.essalud.sispoi.dto.OcR1ReportDTO;

public interface IReportService  {

    byte[] generateReport_oc_r1(String format, Integer dependencyId, Integer year, Integer modification) throws Exception;
    
    List<OcR1ReportDTO> getOcR1Data(Integer dependencyId, Integer year, Integer modification);
    
}
