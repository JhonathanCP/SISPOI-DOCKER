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
public class BudgetItemDTO {

    @EqualsAndHashCode.Include
    private Integer idBudgetItem;

    @NotNull
    @Size(max = 10)
    private String codPoFi;

    @NotNull
    @Size(max = 500)
    private String name;

    @Size(max = 500)
    private String description;

    private Boolean active = true;

    @NotNull
    private BudgetTypeDTO budgetType;

    @NotNull
    private BudgetCategoryDTO budgetCategory;

    private LocalDateTime createTime;

}
