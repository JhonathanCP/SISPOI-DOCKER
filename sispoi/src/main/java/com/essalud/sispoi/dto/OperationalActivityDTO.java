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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OperationalActivityDTO {

    @EqualsAndHashCode.Include
    private Integer idOperationalActivity;

    @NotNull
    @Size(max = 16)
    private String sapCode;

    @Size(max = 3)
    private String correlativeCode;

    @NotNull
    @Size(max = 1000)
    private String name;

    @NotNull
    @Size(max = 5000)
    private String description;
    
    private Boolean active = true;

    @NotNull
    private StrategicActionDTO strategicAction;

    @NotNull
    private FormulationNoFileDTO formulation;

    @NotNull
    private FinancialFundDTO financialFund;

    @NotNull
    private ManagementCenterDTO managementCenter;

    @NotNull
    private CostCenterDTO costCenter;

    private MeasurementTypeDTO measurementType;

    @Size(max = 250)
    private String measurementUnit;

    @JsonManagedReference("operationalActivity-goals")
    private List<GoalDTO> goals;

    @JsonManagedReference("operationalActivity-executedGoals")
    private List<ExecutedGoalDTO> executedGoals;

    
    @JsonManagedReference("operationalActivity-monthlyGoals")
    private List<MonthlyGoalDTO> monthlyGoals;

    @JsonManagedReference("operationalActivity-executedMonthlyGoals")
    private List<ExecutedMonthlyGoalDTO> executedMonthlyGoals;

    @NotNull
    private PriorityDTO priority;

    private LocalDateTime createTime;
    
    @NotNull
    private Float goods;
    
    @NotNull
    private Float remuneration;
    
    @NotNull
    private Float services;

    private ActivityFamilyDTO activityFamily;

}
