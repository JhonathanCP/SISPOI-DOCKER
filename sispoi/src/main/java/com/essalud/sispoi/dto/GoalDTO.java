package com.essalud.sispoi.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GoalDTO {

    @EqualsAndHashCode.Include
    private Integer idGoal;

    private Boolean active = true;

    @JsonBackReference("operationalActivity-goals")
    private OperationalActivityDTO operationalActivity;

    @JsonBackReference("activityDetail-goals")
    private ActivityDetailDTO activityDetail;

    @Min(1)
    @Max(4)
    @NotNull
    private Integer goalOrder;

    @NotNull
    private Double value;

}
