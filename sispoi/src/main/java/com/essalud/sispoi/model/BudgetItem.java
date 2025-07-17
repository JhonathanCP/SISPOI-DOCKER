package com.essalud.sispoi.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItem {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBudgetItem;

    @Column(length = 10, nullable = false)
    private String codPoFi;

    @Column(length = 500, nullable = false)
    private String name;

    @Column(length = 500)
    private String description;
    
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "id_budget_type", nullable = false, foreignKey = @ForeignKey(name = "FK_BUDGET_ITEM_BUDGET_TYPE"))
    private BudgetType budgetType;

    @ManyToOne
    @JoinColumn(name = "id_budget_category", nullable = false, foreignKey = @ForeignKey(name = "FK_BUDGET_ITEM_BUDGET_CATEGORY"))
    private BudgetCategory budgetCategory;

    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }

}
