package com.essalud.sispoi.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.Map;

import com.essalud.sispoi.model.enums.MonthEnum;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(OperationalActivityBudgetItemPK.class)
public class OperationalActivityBudgetItem {

    @Id
    private OperationalActivity operationalActivity;

    @Id
    private BudgetItem budgetItem;

    @Id
    @Column(name = "order_item")
    private Integer orderItem;

    @ElementCollection
    @CollectionTable(
        name = "operational_activity_budget_item_month_amount",
        joinColumns = {
            @JoinColumn(name = "id_operational_activity", referencedColumnName = "id_operational_activity"),
            @JoinColumn(name = "id_budget_item", referencedColumnName = "id_budget_item"),
            @JoinColumn(name = "order_item", referencedColumnName = "order_item")
        }
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "month")
    @Column(name = "amount")
    private Map<MonthEnum, Double> monthAmounts = new EnumMap<>(MonthEnum.class);

    @ManyToOne
    @JoinColumn (name = "id_expense_type", referencedColumnName = "idExpenseType", nullable = false)
    private ExpenseType expenseType;

    @ManyToOne
    @JoinColumn (name = "id_financial_fund", referencedColumnName = "idFinancialFund", nullable = false)
    private FinancialFund financialFund;

    private Double estimation;

    private Double proyection;

}