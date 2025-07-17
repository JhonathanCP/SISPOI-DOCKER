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
public class DependencyDTO {

    @EqualsAndHashCode.Include
    private Integer idDependency;

    @NotNull
    @Size(max = 500)
    private String name;

    @Size(max = 500)
    private String description;
    
    private Boolean active = true;

    @NotNull
    private DependencyTypeDTO dependencyType;

    private LocalDateTime createTime;

}
