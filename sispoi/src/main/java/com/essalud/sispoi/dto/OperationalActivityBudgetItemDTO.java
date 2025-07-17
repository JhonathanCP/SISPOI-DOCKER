package com.essalud.sispoi.dto;

import java.util.EnumMap;
import java.util.Map;

import com.essalud.sispoi.model.enums.MonthEnum;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true )
public class OperationalActivityBudgetItemDTO {

    @NotNull
    private OperationalActivityDTO operationalActivity;

    @NotNull
    private BudgetItemDTO budgetItem;

    @NotNull
    private Integer orderItem;

    private Map<MonthEnum, Double> monthAmounts = new EnumMap<>(MonthEnum.class);

    private ExpenseTypeDTO expenseType;

}
