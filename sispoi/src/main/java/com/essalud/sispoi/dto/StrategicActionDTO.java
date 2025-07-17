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
public class StrategicActionDTO {

    @EqualsAndHashCode.Include
    private Integer idStrategicAction;

    @NotNull
    @Size(max = 2)
    private String code;

    @NotNull
    @Size(max = 500)
    private String name;

    private StrategicActionFileDTO strategicActionFile;

    @NotNull
    private StrategicObjectiveDTO strategicObjective;
    
    private Boolean active = true;

    private LocalDateTime createTime;

}
