package com.essalud.sispoi.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcR1ReportDTO {
    
    private String codOe;
    private String oe;
    private String codAe;
    private String ae;
    private String codCg;
    private String cg;
    private String codCc;
    private String cc;
    private String codFf;
    private String ff;
    private String codAo;
    private String ao;
    private String mu;
    private BigDecimal i;
    private BigDecimal ii;
    private BigDecimal iii;
    private BigDecimal iv;
    private BigDecimal tm;
    private BigDecimal rem;
    private BigDecimal bie;
    private BigDecimal serv;
    private BigDecimal tO;
    private String da;
    
}
