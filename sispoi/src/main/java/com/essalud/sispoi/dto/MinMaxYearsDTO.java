package com.essalud.sispoi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinMaxYearsDTO {
    private Integer minYear;
    private Integer maxYear;
}