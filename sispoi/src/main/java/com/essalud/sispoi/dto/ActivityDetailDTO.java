package com.essalud.sispoi.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class ActivityDetailDTO {

    @EqualsAndHashCode.Include
    private Integer idActivityDetail;

    @NotNull
    @Size(max = 500)
    private String name;

    @Size(max = 500)
    private String description;
    
    private Boolean active = true;

    private Boolean head;

    @NotNull
    private StrategicActionDTO strategicAction;

    private LocalDateTime createTime;

    @Size(max = 250)
    private String measurementUnit;

    private FormulationTypeDTO formulationType;

    private ActivityFamilyDTO activityFamily;

    @JsonManagedReference("activityDetail-goals")
    private List<GoalDTO> goals;

    @JsonManagedReference("activityDetail-monthlyGoals")
    private List<MonthlyGoalDTO> monthlyGoals;

}
