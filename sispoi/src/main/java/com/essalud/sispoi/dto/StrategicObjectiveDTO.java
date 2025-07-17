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
public class StrategicObjectiveDTO {

    @EqualsAndHashCode.Include
    private Integer idStrategicObjective;

    @NotNull
    @Size(max = 2)
    private String code;

    @NotNull
    @Size(max = 500)
    private String name;

    private StrategicObjectiveFileDTO strategicObjectiveFile;
    
    private Boolean active = true;

    private LocalDateTime createTime;

    private Integer startYear;

    private Integer endYear;

}
