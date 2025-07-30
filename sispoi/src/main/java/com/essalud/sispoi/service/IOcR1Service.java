package com.essalud.sispoi.service;

import java.util.List;

import com.essalud.sispoi.dto.OcR1ReportDTO;

public interface IOcR1Service {
    
    List<OcR1ReportDTO> getOcR1Data(Integer dependencyId, Integer year, Integer modification);
    
}
