package com.essalud.sispoi.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true )
public class FormulationDTO {

    @EqualsAndHashCode.Include
    private Integer idFormulation;

    private Boolean active = true;

    @NotNull
    private DependencyDTO dependency;

    @NotNull
    private FormulationStateDTO formulationState;

    private FormulationSupportFileDTO formulationSupportFile;

    private LocalDateTime createTime;

    private Integer year;

    private Integer modification;

    private Integer quarter;

    private FormulationTypeDTO formulationType;

    private Double budget;

}
