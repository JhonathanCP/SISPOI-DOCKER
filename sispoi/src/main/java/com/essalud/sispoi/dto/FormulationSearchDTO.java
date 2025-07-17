package com.essalud.sispoi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true )
public class FormulationSearchDTO {

    private DependencyDTO dependency;

    @Min(2000)
    @Max(2100)
    private Integer year;

}
