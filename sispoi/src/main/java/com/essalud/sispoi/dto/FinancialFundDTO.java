package com.essalud.sispoi.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true )
public class FinancialFundDTO {

    @EqualsAndHashCode.Include
    private Integer idFinancialFund;

    @NotNull
    @Size(max = 500)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private DependencyDTO dependency;

    @NotNull
    @Size(max = 10)
    private String codFofi;
    
    private Boolean active = true;

    private LocalDateTime createTime;

}
